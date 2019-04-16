package datamodel.ws.rest;

import entity.ReviewEntity;



public class UpdateReviewReq
{
    private Long reviewId;
    private String newContent;
    private Integer newProductRating;

    
    public UpdateReviewReq()
    {
    }

    public UpdateReviewReq(Long reviewId, String newContent, Integer newProductRating) {
        this.reviewId = reviewId;
        this.newContent = newContent;
        this.newProductRating = newProductRating;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public Integer getNewProductRating() {
        return newProductRating;
    }

    public void setNewProductRating(Integer newProductRating) {
        this.newProductRating = newProductRating;
    }

    
    
    
    
    

}