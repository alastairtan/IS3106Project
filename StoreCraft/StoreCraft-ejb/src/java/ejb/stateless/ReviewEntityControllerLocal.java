/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.ReviewEntity;

/**
 *
 * @author shawn
 */

public interface ReviewEntityControllerLocal {

    public ReviewEntity retrieveReviewByReviewId(Long reviewId);
    
}
