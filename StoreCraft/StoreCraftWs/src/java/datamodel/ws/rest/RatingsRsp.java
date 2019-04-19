package datamodel.ws.rest;

import entity.CategoryEntity;
import java.math.BigDecimal;
import java.util.List;



public class RatingsRsp
{
    private BigDecimal[] result;

    
    
    public RatingsRsp()
    {
    }

    public RatingsRsp(BigDecimal[] result) {
        this.result = result;
    }
    
 
    public BigDecimal[] getResult() {
        return result;
    }

    public void setResult(BigDecimal[] result) {
        this.result = result;
    }
    
    

    
    

}