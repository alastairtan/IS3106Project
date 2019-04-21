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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.CloseEvent;
import util.exception.CreateNewTagException;
import util.exception.DeleteTagException;
import util.exception.InputDataValidationException;
import util.exception.TagNotFoundException;

/**
 *
 * @author Alastair
 */
@Named(value = "tagManagementManagedBean")
@ViewScoped
public class TagManagementManagedBean implements Serializable {

    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;
    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;
    
    //*** For Initial Load ***
    private List<ProductEntity> productEntities;   
    private List<CategoryEntity> categoryEntities;
    private List<TagEntity> tagEntities;
    // ***********************
    
    //*** For Creating Tag ***
    private TagEntity newTagEntity;
    private Long tagIdNew;
    //****************************
    
    //*** For Updating Tag ***
    private TagEntity selectedTagEntity; //updated when user clicks on actions button, used for both viewing and updating
    private boolean isUpdating; //to keep track when user is updating a tag  
    //****************************
    
    public TagManagementManagedBean() {
        isUpdating = false;
        newTagEntity = new TagEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
        productEntities = productEntityControllerLocal.retrieveAllProducts();
        categoryEntities = categoryEntityControllerLocal.retrieveAllLeafCategories();
        tagEntities = tagEntityControllerLocal.retrieveAllTags();
        //System.out.println("constructed");
    }
    
    public void viewTagDetails(ActionEvent event) throws IOException {
        Long tagIdToView = (Long) event.getComponent().getAttributes().get("tagId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("tagIdToView", tagIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewTagDetails.xhtml");
    }
    
    public void updating(ActionEvent event){
        setIsUpdating(true);
        
        //System.out.println("Updating");
    }
    
    public void cancelUpdating(ActionEvent event){
        setIsUpdating(false);
        try { //to reset fields in the dialog
            selectedTagEntity = tagEntityControllerLocal.retrieveTagByTagId(selectedTagEntity.getTagId());
            productEntities = productEntityControllerLocal.retrieveAllProducts();
        } catch (TagNotFoundException ex) {
            Logger.getLogger(TagManagementManagedBean.class.getName()).log(Level.SEVERE, null, ex); //exception should not occur
        }
    }
    
    public void creating(ActionEvent event){
        this.newTagEntity = new TagEntity();
    }
    public void closeViewDialog(CloseEvent event){
        setIsUpdating(false);
        //System.out.println("Close View Dialog");
    }
    
    public void closeCreateDialog(CloseEvent event){
        this.newTagEntity = new TagEntity();
        //System.out.println("Close Create Dialog");
    }
    
    public void saveChanges(ActionEvent event){       
        
        //System.out.println("savechanges");
        
        try
        {
            tagEntityControllerLocal.updateTag(selectedTagEntity);
           
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tag updated successfully", null));
            cancelUpdating(null);
        }
        catch(TagNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating tag: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    
    }
    
    public void deleteTag(ActionEvent event){
        try
        {
            TagEntity tagEntityToDelete = selectedTagEntity;
            
            tagEntityControllerLocal.deleteTag(tagEntityToDelete.getTagId());
            
            tagEntities.remove(tagEntityToDelete);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tag deleted successfully", null));
        }
        catch(TagNotFoundException | DeleteTagException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting product: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void createNewTag(ActionEvent event){
        
        try
        {
            TagEntity te = tagEntityControllerLocal.createNewTagEntity(newTagEntity);
            tagEntities.add(te);
            
            newTagEntity = new TagEntity();
            

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New tag created successfully (Tag ID: " + te.getTagId() + ")", null));
        }
        catch(InputDataValidationException | CreateNewTagException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new tag: " + ex.getMessage(), null));
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

    public TagEntity getNewTagEntity() {
        return newTagEntity;
    }

    public void setNewTagEntity(TagEntity newTagEntity) {
        this.newTagEntity = newTagEntity;
    }

    public Long getTagIdNew() {
        return tagIdNew;
    }

    public void setTagIdNew(Long tagIdNew) {
        this.tagIdNew = tagIdNew;
    }

    public TagEntity getSelectedTagEntity() {
        return selectedTagEntity;
    }

    public void setSelectedTagEntity(TagEntity selectedTagEntity) {
        this.selectedTagEntity = selectedTagEntity;
    }

    public boolean isIsUpdating() {
        return isUpdating;
    }

    public void setIsUpdating(boolean isUpdating) {
        this.isUpdating = isUpdating;
    }
    
    
    
    
    
    
    
}
