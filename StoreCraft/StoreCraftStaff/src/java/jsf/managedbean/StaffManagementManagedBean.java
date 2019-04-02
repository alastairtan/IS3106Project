/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.StaffEntityControllerLocal;
import entity.StaffEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.event.CloseEvent;
import util.exception.StaffNotFoundException;

/**
 *
 * @author Alastair
 */
@Named(value = "staffManagementManagedBean")
@ViewScoped
public class StaffManagementManagedBean implements Serializable {
    
    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;
    

    private List<StaffEntity> staffEntities;

    private StaffEntity selectedStaffEntity;
    
    private StaffEntity updatingStaffEntity;
    
    private boolean isUpdating;

    /**
     * Creates a new instance of StaffManagementManagedBean
     */
    public StaffManagementManagedBean() {
        isUpdating = false;
    }
    
    public void updating(ActionEvent event){
        setIsUpdating(true);
        System.out.println("Updating");
    }
    
    public void closeDialog(CloseEvent event){
        setIsUpdating(false);
        System.out.println("Not Updating");
    }
    
    public void saveChanges(ActionEvent event){       
        
        System.out.println("savechanges");
        
        
        try
        {
            staffEntityControllerLocal.updateStaff(selectedStaffEntity);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product updated successfully", null));
            setIsUpdating(false);
        }
        catch(StaffNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating product: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    
    }
    
    @PostConstruct
    public void postConstruct() {
        staffEntities = staffEntityControllerLocal.retrieveAllStaffs();
        System.out.println("constructed");
    }

    public StaffEntityControllerLocal getStaffEntityControllerLocal() {
        return staffEntityControllerLocal;
    }

    public void setStaffEntityControllerLocal(StaffEntityControllerLocal staffEntityControllerLocal) {
        this.staffEntityControllerLocal = staffEntityControllerLocal;
    }

    public List<StaffEntity> getStaffEntities() {
        return staffEntities;
    }

    public void setStaffEntities(List<StaffEntity> staffEntities) {
        this.staffEntities = staffEntities;
    }

    public StaffEntity getSelectedStaffEntity() {
        return selectedStaffEntity;
    }

    public void setSelectedStaffEntity(StaffEntity selectedStaffEntity) {
        this.selectedStaffEntity = selectedStaffEntity;
    }

    public StaffEntity getUpdatingStaffEntity() {
        return updatingStaffEntity;
    }

    public void setUpdatingStaffEntity(StaffEntity updatingStaffEntity) {
        this.updatingStaffEntity = updatingStaffEntity;
    }

    public boolean isIsUpdating() {
        return isUpdating;
    }

    public void setIsUpdating(boolean isUpdating) {
        this.isUpdating = isUpdating;
    }
    
}
