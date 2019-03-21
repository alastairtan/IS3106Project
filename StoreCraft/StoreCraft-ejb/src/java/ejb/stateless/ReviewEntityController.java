/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import entity.ReviewEntity;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;

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
    
    
    public ReviewEntityController()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public ReviewEntity createNewReview(CustomerEntity customerEntity, ReviewEntity newReviewEntity) throws InputDataValidationException 
    {
        Set<ConstraintViolation<ReviewEntity>> constraintViolations = validator.validate(newReviewEntity);
        
        if(constraintViolations.isEmpty())
        {
            entityManager.persist(newReviewEntity);
            entityManager.flush();
            
            return newReviewEntity;
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
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
