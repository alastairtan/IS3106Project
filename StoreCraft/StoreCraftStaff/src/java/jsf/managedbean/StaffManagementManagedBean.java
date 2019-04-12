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
import java.util.EnumSet;
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
    
    //*** For Initial Load ***
    private List<StaffTypeEnum> staffTypeEnums;
    // ***********************
    
    /* to create new Staff*/
    private StaffEntity newStaffEntity;
    private StaffTypeEnum newStaffTypeEnum;
    /**************/
    
    //*** For filtering ***
    private List<String> filterStaffType;
    private String condition;
    //*********************

    public StaffManagementManagedBean() {
        isUpdating = false;
        newStaffEntity = new StaffEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
        staffTypeEnums = new ArrayList<StaffTypeEnum>(EnumSet.allOf(StaffTypeEnum.class));
        filterStaffType = new ArrayList<String>();
        staffEntities = staffEntityControllerLocal.retrieveAllStaffs();
        //System.out.println("constructed");
    }
    
    public void updating(ActionEvent event){
        setIsUpdating(true);
        System.out.println("Updating");
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
    
    
    public void filterStaff()
    {
        
        
        if(filterStaffType != null && filterStaffType.size() > 0)
        {
            System.out.println("inside filter staff");
            staffEntities = staffEntityControllerLocal.filterStaffsByStaffTypeEnum(filterStaffType, condition); 
            System.out.println("inside filter staff" + filterStaffType.size());
        }
        else
        {
            staffEntities = staffEntityControllerLocal.retrieveAllStaffs();
        }
    }
    
    public void deleteStaff(ActionEvent event){
        try
        {
            System.out.println("reached0");
            StaffEntity staffEntityToDelete = selectedStaffEntity;
            System.out.println("reached");
            staffEntityControllerLocal.deleteStaff(staffEntityToDelete.getStaffId());
            System.out.println("reached2");
            staffEntities.remove(staffEntityToDelete);
            System.out.println("reached3");
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
    

    public List<StaffTypeEnum> getStaffTypeEnums() {
        return staffTypeEnums;
    }

    public void setStaffTypeEnums(List<StaffTypeEnum> staffTypeEnums) {
        this.staffTypeEnums = staffTypeEnums;
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

    public List<String> getFilterStaffType() {
        return filterStaffType;
    }

    public void setFilterStaffType(List<String> filterStaffType) {
        this.filterStaffType = filterStaffType;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    
}
