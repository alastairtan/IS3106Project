/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import entity.ProductEntity;
import entity.ReviewEntity;
import entity.StaffEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ProductNotFoundException;
import util.exception.StaffNotFoundException;

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

    @EJB(name = "StaffEntityControllerLocal")
    private StaffEntityControllerLocal staffEntityControllerLocal;

    public ReviewEntityController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public ReviewEntity retrieveReviewByReviewId(Long reviewId) throws NoResultException {
        Query query = entityManager.createQuery("SELECT re FROM ReviewEntity re WHERE re.reviewId = :inReviewId");
        query.setParameter("inReviewId", reviewId);

        return (ReviewEntity) query.getSingleResult();
    }

    @Override
    public List<ReviewEntity> retrieveReviewsForProduct(Long productId) throws NoResultException {
        Query query = entityManager.createQuery("SELECT re FROM ReviewEntity re WHERE re.productEntity.productId = :inProductId");
        query.setParameter("inProductId", productId);

        return query.getResultList();
    }

    @Override
    public ReviewEntity createNewReview(Long customerId, String content, Integer productRating, Long productId) throws InputDataValidationException, CustomerNotFoundException, ProductNotFoundException {
        ReviewEntity newReviewEntity = new ReviewEntity(content, productRating, new Date());
        CustomerEntity reviewingCustomer = customerEntityControllerLocal.retrieveCustomerByCustomerId(customerId);
        ProductEntity reviewedProduct = productEntityControllerLocal.retrieveProductByProductId(productId);

        newReviewEntity.setCustomerEntity(reviewingCustomer);
        reviewingCustomer.getReviewEntities().add(newReviewEntity);

        newReviewEntity.setProductEntity(reviewedProduct);
        reviewedProduct.getReviewEntities().add(newReviewEntity);

        Set<ConstraintViolation<ReviewEntity>> constraintViolations = validator.validate(newReviewEntity);

        if (constraintViolations.isEmpty()) {
            reviewedProduct.getReviewEntities().add(newReviewEntity);
            entityManager.persist(newReviewEntity);
            entityManager.flush();

            return newReviewEntity;
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public ReviewEntity deleteReview(Long reviewId) throws NoResultException {
        ReviewEntity reviewToDelete = retrieveReviewByReviewId(reviewId);
        CustomerEntity customer = reviewToDelete.getCustomerEntity();
        ProductEntity product = reviewToDelete.getProductEntity();

        customer.getReviewEntities().remove(reviewToDelete);
        reviewToDelete.setCustomerEntity(null);

        reviewToDelete.setProductEntity(null);
        product.getReviewEntities().remove(reviewToDelete);

        entityManager.remove(reviewToDelete);
        entityManager.flush();

        return reviewToDelete;
    }

    @Override
    public ReviewEntity updateReview(Long reviewId, String newContent, Integer newProductRating) throws NoResultException {
        ReviewEntity reviewToUpdate = retrieveReviewByReviewId(reviewId);
        reviewToUpdate.setContent(newContent);
        reviewToUpdate.setProductRating(newProductRating);

        entityManager.flush();

        return reviewToUpdate;
    }

    @Override
    public ReviewEntity replyReview(Long reviewIdToReply, ReviewEntity replyReviewEntity, Long staffId) throws InputDataValidationException, StaffNotFoundException {
        try {
            ReviewEntity reviewEntityToReply = retrieveReviewByReviewId(reviewIdToReply);
            StaffEntity staffReplying = staffEntityControllerLocal.retrieveStaffByStaffId(staffId);
            replyReviewEntity.setParentReviewEntity(reviewEntityToReply);
            replyReviewEntity.setStaffEntity(staffReplying);

            Set<ConstraintViolation<ReviewEntity>> constraintViolations = validator.validate(replyReviewEntity);

            if (constraintViolations.isEmpty()) {
                reviewEntityToReply.setReplyReviewEntity(replyReviewEntity);
                staffReplying.getReviewEntities().add(replyReviewEntity);
                entityManager.persist(replyReviewEntity);
                entityManager.flush();

                return replyReviewEntity;
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (StaffNotFoundException ex) {
            throw new StaffNotFoundException();
        }
    }

    @Override
    public List<ReviewEntity> retrieveAllRootReviewEntities() {
        Query query = entityManager.createQuery("SELECT re FROM ReviewEntity re WHERE re.customerEntity != NULL AND re.parentReviewEntity = NULL");
        List<ReviewEntity> rootReviews = query.getResultList();
        for (ReviewEntity r : rootReviews) {
            r.getProductEntity();
            r.getCustomerEntity();
            lazilyLoadReviewReplies(r);
        }
        return rootReviews;
    }

    public void lazilyLoadReviewReplies(ReviewEntity reviewEntity){
 
        ReviewEntity reply = reviewEntity.getReplyReviewEntity();
        
        if (reply == null){
            return;
        } else {
            lazilyLoadReviewReplies(reply);
        }
        
    }

    @Override
    public List<ReviewEntity> getReviewChain(Long rootReviewEntityId) {
        ReviewEntity rootReviewEntity = retrieveReviewByReviewId(rootReviewEntityId);
        List<ReviewEntity> reviewChain = new ArrayList<>();

        reviewChain.add(rootReviewEntity);

        while (rootReviewEntity.getReplyReviewEntity() != null) {
            ReviewEntity reviewReply = rootReviewEntity.getReplyReviewEntity();
            reviewChain.add(reviewReply);
            rootReviewEntity = reviewReply;
        }

        return reviewChain;

    }

    @Override
    public List<ReviewEntity> getOutstandingCustomerReviews() {
        Query query = entityManager.createQuery("SELECT re FROM ReviewEntity re WHERE re.customerEntity != NULL AND re.replyReviewEntity = NULL");
        return query.getResultList();
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ReviewEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
