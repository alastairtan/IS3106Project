package datamodel.ws.rest;

import entity.CategoryEntity;
import entity.DiscountCodeEntity;
import java.util.List;



public class RetrieveDiscountCodesRsp
{
    private List<DiscountCodeEntity> discountCodeEntities;

    
    
    public RetrieveDiscountCodesRsp()
    {
    }

    public RetrieveDiscountCodesRsp(List<DiscountCodeEntity> discountCodeEntities) {
        this.discountCodeEntities = discountCodeEntities;
    }

    public List<DiscountCodeEntity> getDiscountCodeEntities() {
        return discountCodeEntities;
    }

    public void setDiscountCodeEntities(List<DiscountCodeEntity> discountCodeEntities) {
        this.discountCodeEntities = discountCodeEntities;
    }

    
    
    
    
    

}