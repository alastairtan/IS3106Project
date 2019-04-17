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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.enumeration.DiscountCodeTypeEnum;
import util.exception.CreateNewDiscountCodeException;
import util.exception.InputDataValidationException;
import util.security.CryptographicHelper;

/**
 *
 * @author shawn
 */
@Named(value = "createDiscountCodeManagedBean")
@ViewScoped
public class CreateDiscountCodeManagedBean implements Serializable {

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

    //*** For Creating ***
    private DiscountCodeEntity newDiscountCodeEntity;
    private List<CustomerEntity> selectedCustomers;
    private List<ProductEntity> selectedProducts;
    private Integer dcTypeEnumIndex;
    //********************

    //*** For filtering ***
    private List<String> filterTagIds;
    private String condition;
    //************

    private Date currentDate;
    private Date minStartDate;
    private Date afterStartDate;

    public CreateDiscountCodeManagedBean() {
        newDiscountCodeEntity = new DiscountCodeEntity();
        selectedCustomers = new ArrayList<>();
        selectedProducts = new ArrayList<>();
        dcTypeEnumIndex = -1;
        currentDate = getToday();
        minStartDate = currentDate;
        afterStartDate = plusOneDay(minStartDate);
    }

    @PostConstruct
    public void PostConstruct() {
        discountCodeEntities = discountCodeEntityControllerLocal.retrieveAllDiscountCodes();
        productEntities = productEntityControllerLocal.retrieveAllProducts();
        customerEntities = customerEntityControllerLocal.retrieveAllCustomer();
        tagEntities = tagEntityControllerLocal.retrieveAllTags();
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

    public void createNewDiscountCode(ActionEvent event) {

        if (selectedCustomers.isEmpty() && selectedProducts.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "At least 1 customer or product must be selected!", null));
            return;
        }

        List<Long> customerEntityIds = new ArrayList<>();

        for (CustomerEntity c : selectedCustomers) {
            customerEntityIds.add(c.getCustomerId());
        }

        List<Long> productEntityIds = new ArrayList<>();

        for (ProductEntity p : selectedProducts) {
            productEntityIds.add(p.getProductId());
        }

        try {
            discountCodeEntityControllerLocal.createNewDiscountCode(newDiscountCodeEntity, customerEntityIds, productEntityIds);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Discount Code Created Successfully", null));

            newDiscountCodeEntity = new DiscountCodeEntity();
            selectedCustomers = new ArrayList<>();
            selectedProducts = new ArrayList<>();
            dcTypeEnumIndex = -1;
        } catch (CreateNewDiscountCodeException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating discount code: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occured: " + ex.getMessage(), null));
        }

    }

    public Date getToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Date clearTime(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
        return new Date();
    }

    public Date plusOneDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.add(Calendar.DAY_OF_YEAR, 1);

        return c.getTime();
    }

    public void update() {
        minStartDate = clearTime(this.newDiscountCodeEntity.getStartDate());
        System.out.print("GET MIN****" + minStartDate);
        afterStartDate = plusOneDay(minStartDate);
        System.out.print("GET AFTER****" + afterStartDate);
        minStartDate = getToday();
    }

    public void generateDiscountCode(ActionEvent event) {
        this.newDiscountCodeEntity.setDiscountCode(CryptographicHelper.getInstance().generateRandomString(6));
    }

    public void setNewDceEnum(ActionEvent event) {
        this.newDiscountCodeEntity.setDiscountCodeTypeEnum(DiscountCodeTypeEnum.values()[dcTypeEnumIndex]);
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

    public DiscountCodeEntity getNewDiscountCodeEntity() {
        return newDiscountCodeEntity;
    }

    public void setNewDiscountCodeEntity(DiscountCodeEntity newDiscountCodeEntity) {
        this.newDiscountCodeEntity = newDiscountCodeEntity;
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
    }

    public List<ProductEntity> getSelectedProducts() {
        return selectedProducts;

    }

    public void setSelectedProducts(List<ProductEntity> selectedProducts) {
        this.selectedProducts = selectedProducts;

    }

    public List<CustomerEntity> getSelectedCustomers() {
        return selectedCustomers;
    }

    public void setSelectedCustomers(List<CustomerEntity> selectedCustomers) {
        this.selectedCustomers = selectedCustomers;
    }

    public CustomerEntity getSelectedCustomerEntity() {
        return selectedCustomerEntity;
    }

    public void setSelectedCustomerEntity(CustomerEntity selectedCustomerEntity) {
        this.selectedCustomerEntity = selectedCustomerEntity;
    }

    public Integer getDcTypeEnumIndex() {
        return dcTypeEnumIndex;
    }

    public void setDcTypeEnumIndex(Integer dcTypeEnumIndex) {
        this.dcTypeEnumIndex = dcTypeEnumIndex;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Date getMinStartDate() {
        return minStartDate;
    }

    public void setMinStartDate(Date minStartDate) {
        this.minStartDate = minStartDate;
    }

    public Date getAfterStartDate() {
        return afterStartDate;
    }

    public void setAfterStartDate(Date afterStartDate) {
        this.afterStartDate = afterStartDate;
    }

}
