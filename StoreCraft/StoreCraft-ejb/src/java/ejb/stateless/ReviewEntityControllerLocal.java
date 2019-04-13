/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.ReviewEntity;
import java.util.List;
import javax.persistence.NoResultException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteReviewException;
import util.exception.InputDataValidationException;
import util.exception.ProductNotFoundException;
import util.exception.ReviewNotFoundException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author shawn
 */

public interface ReviewEntityControllerLocal {

    public ReviewEntity retrieveReviewByReviewId(Long reviewId) throws ReviewNotFoundException;

    public ReviewEntity deleteReview(Long reviewId) throws ReviewNotFoundException, DeleteReviewException;

    public ReviewEntity updateReview(Long reviewId, String newContent, Integer newProductRating) throws ReviewNotFoundException;

    public ReviewEntity createNewReview(Long customerId, String content, Integer productRating, Long productId) throws InputDataValidationException, CustomerNotFoundException, ProductNotFoundException;

    public List<ReviewEntity> retrieveReviewsForProduct(Long productId);

    public ReviewEntity replyReview(Long reviewIdToReply, ReviewEntity replyReviewEntity, Long staffId) throws InputDataValidationException, StaffNotFoundException, ReviewNotFoundException;

    public List<ReviewEntity> retrieveAllRootReviewEntities();

    public List<ReviewEntity> getOutstandingCustomerReviews();

    public List<ReviewEntity> getReviewChain(Long rootReviewEntityId) throws ReviewNotFoundException;
    
}
