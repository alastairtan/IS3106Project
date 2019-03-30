/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.CategoryEntityControllerLocal;
import ejb.stateless.ProductEntityControllerLocal;
import ejb.stateless.TagEntityControllerLocal;
import entity.CategoryEntity;
import entity.ProductEntity;
import entity.TagEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.event.CloseEvent;
import util.exception.ProductNotFoundException;

/**
 *
 * @author shawn
 */
@Named(value = "productManagementManagedBean")
@ViewScoped
public class ProductManagementManagedBean implements Serializable {

    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;
    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;

    private List<ProductEntity> productEntities;

    private ProductEntity selectedProductEntity;
    
    private ProductEntity updatingProductEntity;

    private List<CategoryEntity> categoryEntities;

    private List<TagEntity> tagEntities;

    private boolean isUpdating;
    
    private Long categoryIdUpdate;
    
    private List<String> tagIdsStringUpdate; 

    public ProductManagementManagedBean() {
        isUpdating = false;
    }

    @PostConstruct
    public void postConstruct() {
        productEntities = productEntityControllerLocal.retrieveAllProducts();
        categoryEntities = categoryEntityControllerLocal.retrieveAllLeafCategories();
        tagEntities = tagEntityControllerLocal.retrieveAllTags();
        System.out.println("constructed");
    }

    public void viewProductDetails(ActionEvent event) throws IOException {
        Long productIdToView = (Long) event.getComponent().getAttributes().get("productId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("productIdToView", productIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
    }

    public void updating(ActionEvent event){
        setIsUpdating(true);
        this.categoryIdUpdate = selectedProductEntity.getCategoryEntity().getCategoryId();
        tagIdsStringUpdate = new ArrayList<String>();
        for (TagEntity t : selectedProductEntity.getTagEntities()){
            this.tagIdsStringUpdate.add(t.getTagId().toString());
        }
        System.out.println("Updating");
    }
    
    public void closeDialog(CloseEvent event){
        setIsUpdating(false);
        System.out.println("Not Updating");
    }
    
    public void saveChanges(ActionEvent event){       
        
        System.out.println("savechanges");
        
        List<Long> tagIdsUpdate = null;
        
        if(categoryIdUpdate  == 0)
        {
            categoryIdUpdate = null;
        }
        
        if(tagIdsStringUpdate != null && (!tagIdsStringUpdate.isEmpty()))
        {
            tagIdsUpdate = new ArrayList<>();
            
            for(String tagIdString:tagIdsStringUpdate)
            {
                tagIdsUpdate.add(Long.valueOf(tagIdString));
            }
        }
        
        try
        {
            productEntityControllerLocal.updateProduct(selectedProductEntity, categoryIdUpdate, tagIdsUpdate);
                        
            for(CategoryEntity ce:categoryEntities)
            {
                if(ce.getCategoryId().equals(categoryIdUpdate))
                {
                    selectedProductEntity.setCategoryEntity(ce);
                    break;
                }                
            }
            
            selectedProductEntity.getTagEntities().clear();
            
            for(TagEntity te:tagEntities)
            {
                if(tagIdsUpdate.contains(te.getTagId()))
                {
                    selectedProductEntity.getTagEntities().add(te);
                }                
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product updated successfully", null));
            setIsUpdating(false);
        }
        catch(ProductNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating product: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    
    }
            
    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }

    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }

    public ProductEntity getSelectedProductEntity() {
        return selectedProductEntity;
    }

    public void setSelectedProductEntity(ProductEntity selectedProductEntity) {
        this.selectedProductEntity = selectedProductEntity;
        System.out.println("set selected pe");
    }

    public boolean isIsUpdating() {
        return isUpdating;
    }

    public void setIsUpdating(boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    public ProductEntity getUpdatingProductEntity() {
        return updatingProductEntity;
    }

    public void setUpdatingProductEntity(ProductEntity updatingProductEntity) {
        this.updatingProductEntity = updatingProductEntity;
    }

    public Long getCategoryIdUpdate() {
        return categoryIdUpdate;
    }

    public void setCategoryIdUpdate(Long categoryIdUpdate) {
        this.categoryIdUpdate = categoryIdUpdate;
    }

    public List<String> getTagIdsStringUpdate() {
        return tagIdsStringUpdate;
    }

    public void setTagIdsStringUpdate(List<String> tagIdsStringUpdate) {
        this.tagIdsStringUpdate = tagIdsStringUpdate;
    }
}
