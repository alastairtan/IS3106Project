package datamodel.ws.rest;

import entity.CategoryEntity;
import java.util.List;



public class RetrieveAllCategoriesRsp
{
    private List<CategoryEntity> categoryEntities;

    
    
    public RetrieveAllCategoriesRsp()
    {
    }
    
    
    
    public RetrieveAllCategoriesRsp(List<CategoryEntity> categoryEntities)
    {
        this.categoryEntities = categoryEntities;
    }

    
    
    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }
}