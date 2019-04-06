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
import java.io.Serializable;
import java.util.ArrayList;
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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import util.exception.CategoryNotFoundException;
import util.exception.DeleteProductException;
import util.exception.ProductNotFoundException;

/**
 *
 * @author shawn
 */
@Named(value = "filterProductsByCategoryManagedBean")
@ViewScoped
public class FilterProductsByCategoryManagedBean implements Serializable {
    
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;
    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;
    
    private List<ProductEntity> filteredProductEntities;

    private TreeNode treeNode;
    private TreeNode selectedTreeNode;
    
    private List<TagEntity> tagEntities;
    private List<ProductEntity> productEntities;
    private List<CategoryEntity> categoryEntities;
    
    //*** For Updating Product ***
    private ProductEntity selectedProductEntity; //updated when user clicks on actions button, used for both viewing and updating
    private boolean isUpdating; //to keep track when user is updating a product   
    private Long categoryIdUpdate; //category ID when updating product
    private List<String> tagIdsStringUpdate; //list of tag IDs when updating product
    //****************************
    
    
    public FilterProductsByCategoryManagedBean() {
    }
    
    @PostConstruct
    public void PostConstruct(){
        this.tagEntities = tagEntityControllerLocal.retrieveAllTags();
        this.categoryEntities = categoryEntityControllerLocal.retrieveAllLeafCategories();
        
        List<CategoryEntity> rootCategoryEntities = categoryEntityControllerLocal.retrieveAllRootCategories();
        
        treeNode = new DefaultTreeNode("All", null);
        
        for(CategoryEntity categoryEntity:rootCategoryEntities)
        {
            createTreeNode(categoryEntity, treeNode);
        }
        
        //retain filter even on page refresh
        Long selectedCategoryId = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("productFilterCategory");
        
        if(selectedCategoryId != null){
            for(TreeNode tn:treeNode.getChildren()) //other root categories e.g. electronics and fashion
            {
                CategoryEntity ce = (CategoryEntity)tn.getData();

                if(ce.getCategoryId().equals(selectedCategoryId))
                {
                    selectedTreeNode = tn;
                    break;
                }
                else
                {
                    selectedTreeNode = searchTreeNode(selectedCategoryId, tn);
                }            
            }
        }
        
        filterProduct();
        
    }
    
    public void filterProduct()
    {
        if(selectedTreeNode != null)
        {               
            try
            {
                CategoryEntity ce = (CategoryEntity)selectedTreeNode.getData();
                productEntities = productEntityControllerLocal.filterProductsByCategory(ce.getCategoryId());
            }
            catch(CategoryNotFoundException ex)
            {
                productEntities = productEntityControllerLocal.retrieveAllProducts();
            }
        }
        else
        {
            productEntities = productEntityControllerLocal.retrieveAllProducts();
        }
    }
    
    private void createTreeNode(CategoryEntity categoryEntity, TreeNode parentTreeNode)
    {
        TreeNode treeNode = new DefaultTreeNode(categoryEntity, parentTreeNode);
                
        for(CategoryEntity ce:categoryEntity.getSubCategoryEntities())
        {
            createTreeNode(ce, treeNode);
        }
               
    }
    
    private TreeNode searchTreeNode(Long selectedCategoryId, TreeNode treeNode)
    {
        for(TreeNode tn:treeNode.getChildren())
        {
            CategoryEntity ce = (CategoryEntity)tn.getData();
            
            if(ce.getCategoryId().equals(selectedCategoryId))
            {
                return tn;
            }
            else
            {
                return searchTreeNode(selectedCategoryId, tn);
            }            
        }
        
        return null;
    }
    
    public void updating(ActionEvent event){
        setIsUpdating(true);
        this.categoryIdUpdate = selectedProductEntity.getCategoryEntity().getCategoryId(); //to show the current category when updating
        tagIdsStringUpdate = new ArrayList<>();
        for (TagEntity t : selectedProductEntity.getTagEntities()){
            this.tagIdsStringUpdate.add(t.getTagId().toString()); //to show the current tags when updating
        }
        //System.out.println("Updating");
    }
    
    public void cancelUpdating(ActionEvent event){
        setIsUpdating(false);
        try { //to reset fields in the dialog
            selectedProductEntity = productEntityControllerLocal.retrieveProductByProductId(selectedProductEntity.getProductId());
        } catch (ProductNotFoundException ex) {
            Logger.getLogger(FilterProductsByCategoryManagedBean.class.getName()).log(Level.SEVERE, null, ex); //exception should not occur
        }
    }
    
    public void closeViewDialog(CloseEvent event){
        setIsUpdating(false);
        System.out.println("Close View Dialog");
    }
    
    public void saveChanges(ActionEvent event){       
        
        System.out.println("savechanges");
        
        List<Long> tagIdsUpdate = new ArrayList<>();
        
        if(categoryIdUpdate  == 0)
        {
            categoryIdUpdate = null;
        }
        
        if(tagIdsStringUpdate != null && (!tagIdsStringUpdate.isEmpty())){
            
            for(String tagIdString:tagIdsStringUpdate)
            {
                tagIdsUpdate.add(Long.valueOf(tagIdString));
            }
        }
        
        try
        {
            productEntityControllerLocal.updateProduct(selectedProductEntity, categoryIdUpdate, tagIdsUpdate);
                        
            for(CategoryEntity ce:categoryEntityControllerLocal.retrieveAllLeafCategories()) //to update the selectedProductEntity  (previously not associated with updated categoryEntities)
            {
                if(ce.getCategoryId().equals(categoryIdUpdate))
                {
                    selectedProductEntity.setCategoryEntity(ce);
                    break;
                }                
            }
            
            selectedProductEntity.getTagEntities().clear(); //to update the selectedProductEntity (previously not associated with updated tagEntities)
            
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
    
    public void deleteProduct(ActionEvent event){
        try
        {
            ProductEntity productEntityToDelete = selectedProductEntity;
            productEntityControllerLocal.deleteProduct(productEntityToDelete.getProductId());
            
            productEntities.remove(productEntityToDelete);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product deleted successfully", null));
        }
        catch(ProductNotFoundException | DeleteProductException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting product: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public TreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(TreeNode treeNode) {
        this.treeNode = treeNode;
    }

    public TreeNode getSelectedTreeNode() {
        return selectedTreeNode;
    }

    public void setSelectedTreeNode(TreeNode selectedTreeNode) {
        this.selectedTreeNode = selectedTreeNode;
        
        if(selectedTreeNode != null)
        {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("productFilterCategory", ((CategoryEntity)selectedTreeNode.getData()).getCategoryId());
        }
    }

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }

    public ProductEntity getSelectedProductEntity() {
        return selectedProductEntity;
    }

    public void setSelectedProductEntity(ProductEntity selectedProductEntity) {
        this.selectedProductEntity = selectedProductEntity;
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }

    public boolean isIsUpdating() {
        return isUpdating;
    }

    public void setIsUpdating(boolean isUpdating) {
        this.isUpdating = isUpdating;
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

    public List<ProductEntity> getFilteredProductEntities() {
        return filteredProductEntities;
    }

    public void setFilteredProductEntities(List<ProductEntity> filteredProductEntities) {
        this.filteredProductEntities = filteredProductEntities;
    }

    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }
    
}
