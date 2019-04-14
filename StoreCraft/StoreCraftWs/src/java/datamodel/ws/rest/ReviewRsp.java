package datamodel.ws.rest;

import entity.ReviewEntity;



public class ReviewRsp
{
    private ReviewEntity reviewEntity;

    
    
    public ReviewRsp()
    {
    }

    public ReviewRsp(ReviewEntity reviewEntity) {
        this.reviewEntity = reviewEntity;
    }

    public ReviewEntity getReviewEntity() {
        return reviewEntity;
    }

    public void setReviewEntity(ReviewEntity reviewEntity) {
        this.reviewEntity = reviewEntity;
    }
    
    
    
    
    

}