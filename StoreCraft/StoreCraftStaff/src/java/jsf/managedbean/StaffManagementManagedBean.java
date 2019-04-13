/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.StaffEntityControllerLocal;
import entity.StaffEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.event.CloseEvent;
import util.enumeration.StaffTypeEnum;
import util.exception.CreateNewStaffException;
import util.exception.DeleteStaffException;
import util.exception.InputDataValidationException;
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

    
    /* to create new Staff*/
    private StaffEntity newStaffEntity;
    private StaffTypeEnum newStaffTypeEnum;
    /**************/
    
    //*** For filtering ***
    private List<StaffTypeEnum> filterStaffType;
    private StaffTypeEnum[] staffTypeEnumValues;
    //*********************

    public StaffManagementManagedBean() {
        isUpdating = false;
        newStaffEntity = new StaffEntity();
        staffTypeEnumValues = StaffTypeEnum.values();
        filterStaffType = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct() {
        staffEntities = staffEntityControllerLocal.retrieveAllStaffs();
    }
    
    public void updating(ActionEvent event){
        setIsUpdating(true);
        System.out.println("Updating");
    }
    
    public void cancelUpdating(ActionEvent event){
        setIsUpdating(false);
        try { //to reset fields in the dialog
            selectedStaffEntity = staffEntityControllerLocal.retrieveStaffByStaffId(selectedStaffEntity.getStaffId());
        } catch (StaffNotFoundException ex) {
            Logger.getLogger(FilterProductsByCategoryManagedBean.class.getName()).log(Level.SEVERE, null, ex); //exception should not occur
        }
    }
    
    public void closeDialog(CloseEvent event){
        setIsUpdating(false);
        System.out.println("Not Updating");
    }
    
    public void closeCreateDialog(CloseEvent event){
        this.newStaffEntity = new StaffEntity();
        //System.out.println("Close Create Dialog");
    }
    
    public void saveChanges(ActionEvent event){       
        
        System.out.println("savechanges");
        
        
        try
        {
            staffEntityControllerLocal.updateStaff(selectedStaffEntity);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Staff updated successfully", null));
            setIsUpdating(false);
        }
        catch(StaffNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating staff: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    
    }
    public void createNewStaff(ActionEvent event){
       
        newStaffEntity.setCommunityGoalEntities(null);
        newStaffEntity.setReviewEntities(null);
        
        try
        {
            StaffEntity se = staffEntityControllerLocal.createNewStaff(newStaffEntity);
            staffEntities.add(se);
            
            newStaffEntity = new StaffEntity();
            
            

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New staff created successfully (Staff ID: " + se.getStaffId() + ")", null));
        }
        catch(InputDataValidationException | CreateNewStaffException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new staff: " + ex.getMessage(), null));
        }
    }
    
    public void creating(ActionEvent event){
        this.newStaffEntity = new StaffEntity();
    }
    
    
    public void deleteStaff(ActionEvent event){
        try
        {
            StaffEntity staffEntityToDelete = selectedStaffEntity;
            staffEntityControllerLocal.deleteStaff(staffEntityToDelete.getStaffId());
            staffEntities.remove(staffEntityToDelete);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Staff deleted successfully", null));
        }
        catch(StaffNotFoundException | DeleteStaffException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting staff: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
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

    public StaffEntity getNewStaffEntity() {
        return newStaffEntity;
    }

    public void setNewStaffEntity(StaffEntity newStaffEntity) {
        this.newStaffEntity = newStaffEntity;
    }

    public StaffTypeEnum getNewStaffTypeEnum() {
        return newStaffTypeEnum;
    }

    public void setNewStaffTypeEnum(StaffTypeEnum newStaffTypeEnum) {
        this.newStaffTypeEnum = newStaffTypeEnum;
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

    public List<StaffTypeEnum> getFilterStaffType() {
        return filterStaffType;
    }

    public void setFilterStaffType(List<StaffTypeEnum> filterStaffType) {
        this.filterStaffType = filterStaffType;
    }

    public StaffTypeEnum[] getStaffTypeEnumValues() {
        return staffTypeEnumValues;
    }

    public void setStaffTypeEnumValues(StaffTypeEnum[] staffTypeEnumValues) {
        this.staffTypeEnumValues = staffTypeEnumValues;
    }
    
}
