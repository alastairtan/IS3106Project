/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.CustomerEntityControllerLocal;
import entity.CustomerEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.CloseEvent;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author Win Phong
 */
@Named(value = "customerManagementManagedBean")
@ViewScoped
public class CustomerManagementManagedBean implements Serializable {
    
    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;
    
    // Initial data load
    private List<CustomerEntity> customerEntities;

    private CustomerEntity selectedCustomerEntity;
    private List<CustomerEntity> filteredCustomerEntities;
    
    
    
    public CustomerManagementManagedBean() 
    {
    }
    
    @PostConstruct
    public void postConstruct()
    {
        customerEntities = customerEntityControllerLocal.retrieveAllCustomer();
    }
    
    public void updateCustomer()
    {
        try {
            customerEntityControllerLocal.updateCustomerDetails(selectedCustomerEntity);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer updated successfully", null));
        } 
        catch (CustomerNotFoundException | UpdateCustomerException | InputDataValidationException  ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating customer: " + ex.getMessage(), null));
        } 
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void closeViewDialog(CloseEvent event){
        selectedCustomerEntity = new CustomerEntity();
    }
    
    

    public List<CustomerEntity> getCustomerEntities() {
        return customerEntities;
    }

    public void setCustomerEntities(List<CustomerEntity> customerEntities) {
        this.customerEntities = customerEntities;
    }

    public CustomerEntity getSelectedCustomerEntity() {
        return selectedCustomerEntity;
    }

    public void setSelectedCustomerEntity(CustomerEntity selectedCustomerEntity) {
        this.selectedCustomerEntity = selectedCustomerEntity;
    }

    public List<CustomerEntity> getFilteredCustomerEntities() {
        return filteredCustomerEntities;
    }

    public void setFilteredCustomerEntities(List<CustomerEntity> filteredCustomerEntities) {
        this.filteredCustomerEntities = filteredCustomerEntities;
    }
    
    
    
}
