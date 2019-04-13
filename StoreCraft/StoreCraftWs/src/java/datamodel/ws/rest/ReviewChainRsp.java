package datamodel.ws.rest;

import entity.ReviewEntity;
import java.util.List;



public class ReviewChainRsp
{
    private List<ReviewEntity> reviewEntities;

    
    
    public ReviewChainRsp()
    {
    }
    
    
    
    public ReviewChainRsp(List<ReviewEntity> reviewEntities)
    {
        this.reviewEntities = reviewEntities;
    }

    public List<ReviewEntity> getReviewEntities() {
        return reviewEntities;
    }

    public void setReviewEntities(List<ReviewEntity> reviewEntities) {
        this.reviewEntities = reviewEntities;
    }

    
}