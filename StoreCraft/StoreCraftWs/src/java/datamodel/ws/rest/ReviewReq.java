package datamodel.ws.rest;

import entity.ReviewEntity;



public class ReviewReq
{
    private ReviewEntity reviewEntity;

    
    
    public ReviewReq()
    {
    }

    public ReviewReq(ReviewEntity reviewEntity) {
        this.reviewEntity = reviewEntity;
    }

    public ReviewEntity getReviewEntity() {
        return reviewEntity;
    }

    public void setReviewEntity(ReviewEntity reviewEntity) {
        this.reviewEntity = reviewEntity;
    }
    
    
    
    
    

}