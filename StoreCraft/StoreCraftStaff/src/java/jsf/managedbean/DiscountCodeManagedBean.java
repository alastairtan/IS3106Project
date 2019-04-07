/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.CustomerEntityControllerLocal;
import ejb.stateless.DiscountCodeEntityControllerLocal;
import ejb.stateless.ProductEntityControllerLocal;
import entity.CustomerEntity;
import entity.DiscountCodeEntity;
import entity.ProductEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author shawn
 */
@Named(value = "discountCodeManagedBean")
@ViewScoped
public class DiscountCodeManagedBean implements Serializable{

    @EJB
    private DiscountCodeEntityControllerLocal discountCodeEntityControllerLocal;
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;
    
    //*** For Initial Load ***
    private List<DiscountCodeEntity> discountCodeEntities;
    private List<CustomerEntity> customerEntities;
    private List<ProductEntity> productEntities;
    //************************
    
    private List<DiscountCodeEntity> filteredDiscountCodeEntities;
    
    //*** For Viewing/Updating ***
    private DiscountCodeEntity selectedDiscountCodeEntity;
    private boolean isUpdating;
    private List<String> customerIdsUpdate;
    private List<String> productIdsUpdate;
    //****************************
    
            
  
    public DiscountCodeManagedBean() {
        isUpdating = false;
    }
    
    @PostConstruct
    public void PostConstruct(){
        discountCodeEntities = discountCodeEntityControllerLocal.retrieveAllDiscountCodes();
        productEntities = productEntityControllerLocal.retrieveAllProducts();
        customerEntities = customerEntityControllerLocal.retrieveAllCustomer();
    }
    
    public void createNewDiscountCode(ActionEvent event){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("createDiscountCode.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DiscountCodeManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void viewDiscountCode(ActionEvent event){
        try {
            DiscountCodeEntity dcToUpdate = (DiscountCodeEntity) event.getComponent().getAttributes().get("discountCodeEntityToUpdate");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("discountCodeEntityToUpdate", dcToUpdate);
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewUpdateDiscountCode.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DiscountCodeManagedBean.class.getName()).log(Level.SEVERE, null, ex);
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

    public DiscountCodeEntity getSelectedDiscountCodeEntity() {
        return selectedDiscountCodeEntity;
    }

    public void setSelectedDiscountCodeEntity(DiscountCodeEntity selectedDiscountCodeEntity) {
        this.selectedDiscountCodeEntity = selectedDiscountCodeEntity;
    }

    public boolean isIsUpdating() {
        return isUpdating;
    }

    public void setIsUpdating(boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    public List<String> getCustomerIdsUpdate() {
        return customerIdsUpdate;
    }

    public void setCustomerIdsUpdate(List<String> customerIdsUpdate) {
        this.customerIdsUpdate = customerIdsUpdate;
    }

    public List<String> getProductIdsUpdate() {
        return productIdsUpdate;
    }

    public void setProductIdsUpdate(List<String> productIdsUpdate) {
        this.productIdsUpdate = productIdsUpdate;
    }

    public List<DiscountCodeEntity> getFilteredDiscountCodeEntities() {
        return filteredDiscountCodeEntities;
    }

    public void setFilteredDiscountCodeEntities(List<DiscountCodeEntity> filteredDiscountCodeEntities) {
        this.filteredDiscountCodeEntities = filteredDiscountCodeEntities;
    }
    
    
    
}
