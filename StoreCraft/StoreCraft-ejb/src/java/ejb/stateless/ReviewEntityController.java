/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import entity.ProductEntity;
import entity.ReviewEntity;
import java.util.Date;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ProductNotFoundException;

/**
 *
 * @author shawn
 */
@Stateless
@Local(ReviewEntityControllerLocal.class)
public class ReviewEntityController implements ReviewEntityControllerLocal {

    @PersistenceContext(unitName = "StoreCraft-ejbPU")
    private EntityManager entityManager;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    @EJB 
    private CustomerEntityControllerLocal customerEntityControllerLocal;
    
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    
    
    public ReviewEntityController()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public ReviewEntity retrieveReviewByReviewId (Long reviewId){
        Query query = entityManager.createQuery("SELECT re FROM ReviewEntity re WHERE re.reviewId = :inReviewId");
        query.setParameter("inReviewId", reviewId);
        
        return (ReviewEntity) query.getSingleResult();
    }

    public ReviewEntity createNewReview(Long customerId, String content, Integer productRating, Long productId) throws InputDataValidationException, CustomerNotFoundException, ProductNotFoundException 
    {
        ReviewEntity newReviewEntity = new ReviewEntity(content, productRating, new Date());
        CustomerEntity reviewingCustomer = customerEntityControllerLocal.retrieveCustomerByCustomerId(customerId);
        ProductEntity reviewedProduct = productEntityControllerLocal.retrieveProductByProductId(productId);
        
        newReviewEntity.setCustomerEntity(reviewingCustomer);
        
        Set<ConstraintViolation<ReviewEntity>> constraintViolations = validator.validate(newReviewEntity);
        
        if(constraintViolations.isEmpty())
        {
            reviewedProduct.getReviewEntities().add(newReviewEntity);
            entityManager.persist(newReviewEntity);
            entityManager.flush();
            
            return newReviewEntity;
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    public ReviewEntity deleteReview(Long reviewId){
        return null; 
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ReviewEntity>> constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
