package datamodel.ws.rest;

import entity.CategoryEntity;
import java.util.List;



public class CategoryRsp
{
    private CategoryEntity categoryEntity;

    
    
    public CategoryRsp()
    {
    }
    
    
    
    public CategoryRsp(CategoryEntity categoryEntity)
    {
        this.categoryEntity = categoryEntity;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    
    

}