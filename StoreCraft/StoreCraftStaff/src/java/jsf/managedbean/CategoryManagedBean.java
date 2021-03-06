/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.CategoryEntityControllerLocal;
import entity.CategoryEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.CloseEvent;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;

/**
 *
 * @author bryan
 */
@Named(value = "categoryManagedBean")
@ViewScoped
public class CategoryManagedBean implements Serializable {

    @EJB(name = "CategoryEntityControllerLocal")
    private CategoryEntityControllerLocal categoryEntityControllerLocal;

    //For Initial Load
    private List<CategoryEntity> categories;
    //****************

    private List<CategoryEntity> categoriesCanParent;

    //For Search All Fields
    private List<CategoryEntity> filteredCategories;
    //****************

    //For Creating
    private CategoryEntity newCategory;
    private Long parentCategoryId;
    //************

    //For Updating
    private boolean isUpdating;
    private CategoryEntity selectedCategory;
    //***********

    public CategoryManagedBean() {
        categories = new ArrayList<>();
        newCategory = new CategoryEntity();
        selectedCategory = new CategoryEntity();
        categoriesCanParent = new ArrayList<>();
    }

    @PostConstruct
    public void PostConstruct() {
        this.categories = categoryEntityControllerLocal.retrieveAllCategories();
        for (CategoryEntity c : this.categories) {
            if (c.getProductEntities() == null || c.getProductEntities().isEmpty()) {
                this.categoriesCanParent.add(c);
            }
        }
    }
    
    public void creating(ActionEvent event){
        this.newCategory = new CategoryEntity();
    }

    public void updating(ActionEvent event) {
        setIsUpdating(true);
    }

    public void cancelUpdating(ActionEvent event) {
        setIsUpdating(false);
    }

    public void closeViewDialog(CloseEvent event) {
        setIsUpdating(false);
        selectedCategory = new CategoryEntity();
    }

    public void closeCreateDialog(CloseEvent event) {
        newCategory = new CategoryEntity();
        setIsUpdating(false);
        parentCategoryId = null;
    }

    public void updateCategory(ActionEvent event) {
        try {
            categoryEntityControllerLocal.updateCategory(selectedCategory, null);
            this.categories = categoryEntityControllerLocal.retrieveAllCategories();
            for (CategoryEntity c : this.categories) {
                if (c.getProductEntities() == null || c.getProductEntities().isEmpty()) {
                    this.categoriesCanParent.add(c);
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category updated successfully", null));
            cancelUpdating(null);
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating category: " + ex.getMessage(), null));
        } catch (CategoryNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating category: " + ex.getMessage(), null));
        } catch (UpdateCategoryException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating category: " + ex.getMessage(), null));
        }

    }

    public void createCategory(ActionEvent event) {

        try {
            categoryEntityControllerLocal.createNewCategoryEntity(newCategory, parentCategoryId);
            this.categories = categoryEntityControllerLocal.retrieveAllCategories();
            for (CategoryEntity c : this.categories) {
                if (c.getProductEntities() == null || c.getProductEntities().isEmpty()) {
                    this.categoriesCanParent.add(c);
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category created successfully", null));
        } catch (InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating category: " + ex.getMessage(), null));
        } catch (CreateNewCategoryException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating category: " + ex.getMessage(), null));
        }
    }

    public void deleteCategory(ActionEvent event) {
        try {
            categoryEntityControllerLocal.deleteCategory(selectedCategory.getCategoryId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Category deleted successfully", null));
            categories.remove(selectedCategory);
        } catch (CategoryNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting category: " + ex.getMessage(), null));
        } catch (DeleteCategoryException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting catgory: " + ex.getMessage(), null));
        }
    }

    public CategoryEntityControllerLocal getCategoryEntityControllerLocal() {
        return categoryEntityControllerLocal;
    }

    public void setCategoryEntityControllerLocal(CategoryEntityControllerLocal categoryEntityControllerLocal) {
        this.categoryEntityControllerLocal = categoryEntityControllerLocal;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    public List<CategoryEntity> getFilteredCategories() {
        return filteredCategories;
    }

    public void setFilteredCategories(List<CategoryEntity> filteredCategories) {
        this.filteredCategories = filteredCategories;
    }

    public CategoryEntity getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(CategoryEntity newCategory) {
        this.newCategory = newCategory;
    }

    public boolean isIsUpdating() {
        return isUpdating;
    }

    public void setIsUpdating(boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    public CategoryEntity getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(CategoryEntity selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public List<CategoryEntity> getCategoriesCanParent() {
        return categoriesCanParent;
    }

    public void setCategoriesCanParent(List<CategoryEntity> categoriesCanParent) {
        this.categoriesCanParent = categoriesCanParent;
    }
}
