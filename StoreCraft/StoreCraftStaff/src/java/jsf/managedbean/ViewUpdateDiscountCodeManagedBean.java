/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.CustomerEntityControllerLocal;
import ejb.stateless.DiscountCodeEntityControllerLocal;
import ejb.stateless.ProductEntityControllerLocal;
import ejb.stateless.TagEntityControllerLocal;
import entity.CustomerEntity;
import entity.DiscountCodeEntity;
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
import util.enumeration.DiscountCodeTypeEnum;
import util.exception.CustomerNotFoundException;
import util.exception.DiscountCodeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidDiscountCodeException;
import util.exception.ProductNotFoundException;
import util.security.CryptographicHelper;

/**
 *
 * @author shawn
 */
@Named(value = "viewUpdateDiscountCodeManagedBean")
@ViewScoped
public class ViewUpdateDiscountCodeManagedBean implements Serializable {

    @EJB
    private DiscountCodeEntityControllerLocal discountCodeEntityControllerLocal;
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;
    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;

    //*** For Initial Load ***
    private List<DiscountCodeEntity> discountCodeEntities;
    private List<CustomerEntity> customerEntities;
    private List<ProductEntity> productEntities;
    private List<TagEntity> tagEntities;
    //************************

    private List<CustomerEntity> filteredCustomerEntities;
    private List<ProductEntity> filteredProductEntities;

    //*** For Product Table ***
    private ProductEntity selectedProductEntity;
    //*************************

    //*** For Customer Table ***
    private CustomerEntity selectedCustomerEntity;
    //*************************

    //*** For Updating ***
    private DiscountCodeEntity selectedDiscountCodeEntity;
    private List<CustomerEntity> selectedCustomersUpdate;
    private List<ProductEntity> selectedProductsUpdate;
    private Integer dcTypeEnumIndex;
    private boolean isUpdating;
    //********************

    //*** For filtering ***
    private List<String> filterTagIds;
    private String condition;
    //************

    public ViewUpdateDiscountCodeManagedBean() {
        isUpdating = false;
    }

    @PostConstruct
    public void PostConstruct() {
        discountCodeEntities = discountCodeEntityControllerLocal.retrieveAllDiscountCodes();
        productEntities = productEntityControllerLocal.retrieveAllProducts();
        customerEntities = customerEntityControllerLocal.retrieveAllCustomer();
        tagEntities = tagEntityControllerLocal.retrieveAllTags();
        this.selectedDiscountCodeEntity = (DiscountCodeEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("discountCodeEntityToUpdate");
        this.selectedCustomersUpdate = this.selectedDiscountCodeEntity.getCustomerEntities();
        this.selectedProductsUpdate = this.selectedDiscountCodeEntity.getProductEntities();
        this.dcTypeEnumIndex = this.selectedDiscountCodeEntity.getDiscountCodeTypeEnum().ordinal(); 
    }
    
    public void saveChanges(ActionEvent event){
        if (selectedCustomersUpdate.isEmpty() && selectedProductsUpdate.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "At least 1 customer or product must be selected!", null));
            return;
        }
        
        List<Long> customerEntityIds = new ArrayList<>();
        
        for (CustomerEntity c : selectedCustomersUpdate){
            customerEntityIds.add(c.getCustomerId());
        }
        
        List<Long> productEntityIds = new ArrayList<>();
        
        for (ProductEntity p : selectedProductsUpdate){
            productEntityIds.add(p.getProductId());
        }
        
        
        try {
            discountCodeEntityControllerLocal.updateDiscountCode(selectedDiscountCodeEntity, customerEntityIds, productEntityIds);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Discount Code Created Successfully", null));
            cancelUpdating(null);
        } catch (DiscountCodeNotFoundException | InvalidDiscountCodeException | InputDataValidationException | CustomerNotFoundException | ProductNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating discount code: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occured: " + ex.getMessage(), null));
        }
    }
    
    public void updating(ActionEvent event){
        setIsUpdating(true);
    }
    
    public void cancelUpdating(ActionEvent event){
        setIsUpdating(false);
        try { //restore correct fields
            selectedDiscountCodeEntity = discountCodeEntityControllerLocal.retrieveDiscountCodeByDiscountCodeId(selectedDiscountCodeEntity.getDiscountCodeId());
        } catch (DiscountCodeNotFoundException ex) {
            Logger.getLogger(FilterProductsByCategoryManagedBean.class.getName()).log(Level.SEVERE, null, ex); //exception should not occur
        }
    }

    public void generateDiscountCode(ActionEvent event) {
        this.selectedDiscountCodeEntity.setDiscountCode(CryptographicHelper.getInstance().generateRandomString(6));
    }

    public void setNewDceEnum(ActionEvent event) {
        this.selectedDiscountCodeEntity.setDiscountCodeTypeEnum(DiscountCodeTypeEnum.values()[dcTypeEnumIndex]);
    }

    public void filterProduct() {

        System.out.println("Filter");
        List<Long> tagIds = new ArrayList<>();

        if (filterTagIds != null && filterTagIds.size() > 0) {
            for (String tagId : filterTagIds) {
                tagIds.add(Long.valueOf(tagId));
            }

            productEntities = productEntityControllerLocal.filterProductsByTags(tagIds, condition);
        } else {
            productEntities = productEntityControllerLocal.retrieveAllProducts();
        }
    }

    public List<DiscountCodeEntity> getDiscountCodeEntities() {
        return discountCodeEntities;
    }

    public void setDiscountCodeEntities(List<DiscountCodeEntity> discountCodeEntities) {
        this.discountCodeEntities = discountCodeEntities;
    }

    public List<CustomerEntity> getCustomerEntities() {
        return customerEntities;
    }

    public void setCustomerEntities(List<CustomerEntity> customerEntities) {
        this.customerEntities = customerEntities;
    }

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }

    public List<CustomerEntity> getFilteredCustomerEntities() {
        return filteredCustomerEntities;
    }

    public void setFilteredCustomerEntities(List<CustomerEntity> filteredCustomerEntities) {
        this.filteredCustomerEntities = filteredCustomerEntities;
    }

    public List<ProductEntity> getFilteredProductEntities() {
        return filteredProductEntities;
    }

    public void setFilteredProductEntities(List<ProductEntity> filteredProductEntities) {
        this.filteredProductEntities = filteredProductEntities;
    }

    public ProductEntity getSelectedProductEntity() {
        return selectedProductEntity;
    }

    public void setSelectedProductEntity(ProductEntity selectedProductEntity) {
        this.selectedProductEntity = selectedProductEntity;
    }

    public CustomerEntity getSelectedCustomerEntity() {
        return selectedCustomerEntity;
    }

    public void setSelectedCustomerEntity(CustomerEntity selectedCustomerEntity) {
        this.selectedCustomerEntity = selectedCustomerEntity;
    }

    public DiscountCodeEntity getSelectedDiscountCodeEntity() {
        return selectedDiscountCodeEntity;
    }

    public void setSelectedDiscountCodeEntity(DiscountCodeEntity selectedDiscountCodeEntity) {
        this.selectedDiscountCodeEntity = selectedDiscountCodeEntity;
    }

    public List<CustomerEntity> getSelectedCustomersUpdate() {
        return selectedCustomersUpdate;
    }

    public void setSelectedCustomersUpdate(List<CustomerEntity> selectedCustomersUpdate) {
        this.selectedCustomersUpdate = selectedCustomersUpdate;
    }

    public List<ProductEntity> getSelectedProductsUpdate() {
        return selectedProductsUpdate;
    }

    public void setSelectedProductsUpdate(List<ProductEntity> selectedProductsUpdate) {
        this.selectedProductsUpdate = selectedProductsUpdate;
    }

    public Integer getDcTypeEnumIndex() {
        return dcTypeEnumIndex;
    }

    public void setDcTypeEnumIndex(Integer dcTypeEnumIndex) {
        this.dcTypeEnumIndex = dcTypeEnumIndex;
    }

    public boolean isIsUpdating() {
        return isUpdating;
    }

    public void setIsUpdating(boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    public List<String> getFilterTagIds() {
        return filterTagIds;
    }

    public void setFilterTagIds(List<String> filterTagIds) {
        this.filterTagIds = filterTagIds;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

}
