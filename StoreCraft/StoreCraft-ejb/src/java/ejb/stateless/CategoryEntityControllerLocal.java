/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CategoryEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;

/**
 *
 * @author shawn
 */
@Local
public interface CategoryEntityControllerLocal {

    public CategoryEntity createNewCategoryEntity(CategoryEntity newCategoryEntity, Long parentCategoryId) throws InputDataValidationException, CreateNewCategoryException;

    public List<CategoryEntity> retrieveAllCategories();

    public List<CategoryEntity> retrieveAllRootCategories();

    public List<CategoryEntity> retrieveAllLeafCategories();

    public CategoryEntity retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException;

    public void updateCategory(CategoryEntity categoryEntity, Long parentCategoryId) throws InputDataValidationException, CategoryNotFoundException, UpdateCategoryException;

    public void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException;

    public List<CategoryEntity> retrieveAllCategoriesWithoutProduct();
    
}
