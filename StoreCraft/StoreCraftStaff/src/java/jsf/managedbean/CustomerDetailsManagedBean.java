/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.CustomerEntityControllerLocal;
import entity.CustomerEntity;
import entity.DiscountCodeEntity;
import entity.ReviewEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Win Phong
 */
@Named(value = "customerDetailsManagedBean")
@ViewScoped
public class CustomerDetailsManagedBean implements Serializable {

    public List<SaleTransactionLineItemEntity> getFilteredSaleTransactionLineItemEntities() {
        return filteredSaleTransactionLineItemEntities;
    }

    public void setFilteredSaleTransactionLineItemEntities(List<SaleTransactionLineItemEntity> filteredSaleTransactionLineItemEntities) {
        this.filteredSaleTransactionLineItemEntities = filteredSaleTransactionLineItemEntities;
    }

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    // Initial retrieve
    private CustomerEntity customerEntity;
    private List<DiscountCodeEntity> discountCodeEntities;
    private List<ReviewEntity> reviewEntities;
    private List<SaleTransactionEntity> saleTransactionEntities;
    
    // Filtered list
    private List<DiscountCodeEntity> filteredDiscountCodeEntities;
    private List<ReviewEntity> filteredReviewEntities;
    private List<SaleTransactionEntity> filteredSaleTransactionEntities;
    
    private List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities;
    private List<SaleTransactionLineItemEntity> filteredSaleTransactionLineItemEntities;
    private SaleTransactionEntity currentSaleTransactionEntity;
    
    public CustomerDetailsManagedBean() 
    {
    }
    
    @PostConstruct
    public void postConstruct()
    {
        try 
        {
            Long customerId = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("customerId"));
            customerEntity = customerEntityControllerLocal.retrieveCustomerByCustomerId(customerId);

            discountCodeEntities = customerEntity.getDiscountCodeEntities();
            reviewEntities = customerEntity.getReviewEntities();
            saleTransactionEntities = customerEntity.getSaleTransactionEntities();
        }
        catch (NumberFormatException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Customer ID not provided", null));
        }
        catch (CustomerNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the customer record: " + ex.getMessage(), null));
        }
    }
    
    
    

    public List<DiscountCodeEntity> getDiscountCodeEntities() {
        return discountCodeEntities;
    }

    public void setDiscountCodeEntities(List<DiscountCodeEntity> discountCodeEntities) {
        this.discountCodeEntities = discountCodeEntities;
    }

    public List<ReviewEntity> getReviewEntities() {
        return reviewEntities;
    }

    public void setReviewEntities(List<ReviewEntity> reviewEntities) {
        this.reviewEntities = reviewEntities;
    }

    public List<SaleTransactionEntity> getSaleTransactionEntities() {
        return saleTransactionEntities;
    }

    public void setSaleTransactionEntities(List<SaleTransactionEntity> saleTransactionEntities) {
        this.saleTransactionEntities = saleTransactionEntities;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public List<DiscountCodeEntity> getFilteredDiscountCodeEntities() {
        return filteredDiscountCodeEntities;
    }

    public void setFilteredDiscountCodeEntities(List<DiscountCodeEntity> filteredDiscountCodeEntities) {
        this.filteredDiscountCodeEntities = filteredDiscountCodeEntities;
    }

    public List<ReviewEntity> getFilteredReviewEntities() {
        return filteredReviewEntities;
    }

    public void setFilteredReviewEntities(List<ReviewEntity> filteredReviewEntities) {
        this.filteredReviewEntities = filteredReviewEntities;
    }

    public List<SaleTransactionEntity> getFilteredSaleTransactionEntities() {
        return filteredSaleTransactionEntities;
    }

    public void setFilteredSaleTransactionEntities(List<SaleTransactionEntity> filteredSaleTransactionEntities) {
        this.filteredSaleTransactionEntities = filteredSaleTransactionEntities;
    }

    public List<SaleTransactionLineItemEntity> getSaleTransactionLineItemEntities() {
        return saleTransactionLineItemEntities;
    }

    public void setSaleTransactionLineItemEntities(List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities) {
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }

    public SaleTransactionEntity getCurrentSaleTransactionEntity() {
        return currentSaleTransactionEntity;
    }

    public void setCurrentSaleTransactionEntity(SaleTransactionEntity currentSaleTransactionEntity) {
        this.currentSaleTransactionEntity = currentSaleTransactionEntity;
    }
    
    
    
}
