/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.CommunityGoalEntityControllerLocal;
import entity.CommunityGoalEntity;
import entity.StaffEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.CloseEvent;
import util.exception.CommunityGoalNotFoundException;
import util.exception.CreateNewCommunityGoalException;
import util.exception.InputDataValidationException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author bryan
 */
@Named(value = "communityGoalsManagedBean")
@RequestScoped
public class CommunityGoalsManagedBean {

    @EJB
    private CommunityGoalEntityControllerLocal communityGoalEntityControllerLocal;

    private List<CommunityGoalEntity> communityGoals;
    
    private CommunityGoalEntity newCommunityGoal;
    
    private CommunityGoalEntity selectedCommunityGoal;
    
    
    private List<String> countries;
    private Date currentDate;
    private Date afterStartDate;
    
    public CommunityGoalsManagedBean() {
        countries= new ArrayList<>();
        countries.add("Singapore");
        countries.add("China");
        countries.add("India");
        countries.add("Malaysia");
        countries.add("Frozen Throne");
        currentDate = new Date();
        afterStartDate = new Date();
        newCommunityGoal = new CommunityGoalEntity();
    }
    
    @PostConstruct
    private void postConstruct(){
        communityGoals = communityGoalEntityControllerLocal.retrieveAllCommunityGoals();
    }
    
    public void closeUpdateDialog(CloseEvent event){
        selectedCommunityGoal = new CommunityGoalEntity();
        currentDate = new Date();
        afterStartDate = new Date();
    }
    
    public void closeCreateDialog(CloseEvent event){
        newCommunityGoal = new CommunityGoalEntity();
    }
    
    public Date afterDate(ValueChangeEvent event){
        
        afterStartDate = selectedCommunityGoal.getStartDate();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        c.add(Calendar.DATE, 1); //same with c.add(Calendar.DAY_OF_MONTH, 1);

        Date currentDatePlusOne = c.getTime();
        return currentDatePlusOne;
    }
    
    public void updateCommunityGoal(ActionEvent event){
        try {
            communityGoalEntityControllerLocal.updateCommunityGoal(newCommunityGoal, newCommunityGoal.getCommunityGoalId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Community Goal updated successfully", null));
        } catch (InputDataValidationException | CommunityGoalNotFoundException ex) {
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating community goal: " + ex.getMessage(), null));
        }  
    }
    
    public void createCommunityGoal(ActionEvent event){
        
        //StaffEntity staff = (StaffEntity)  FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStaffEntity");
        try {
            System.err.println("sfffeferferfff");
            communityGoalEntityControllerLocal.createNewCommunityGoal(newCommunityGoal, 1L);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Community Goal successfully", null));
            System.err.println("sffffererg55y6566");
        } catch (InputDataValidationException | StaffNotFoundException | CreateNewCommunityGoalException ex) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating community goal: " + ex.getMessage(), null));
        }
    }
    
    public void deleteCommunityGoal(ActionEvent event){
        try {
            communityGoalEntityControllerLocal.deleteCommunityGoal(selectedCommunityGoal.getCommunityGoalId());
            communityGoals.remove(selectedCommunityGoal);
        } catch (CommunityGoalNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting community goal: " + ex.getMessage(), null));
        }
    }
    
    public void doSomething(ActionEvent event){
        System.out.println("why dialog is not showing?");
    }

    public List<CommunityGoalEntity> getCommunityGoals() {
        return communityGoals;
    }

    public void setCommunityGoals(List<CommunityGoalEntity> communityGoals) {
        this.communityGoals = communityGoals;
    }

    public CommunityGoalEntity getNewCommunityGoal() {
        return newCommunityGoal;
    }

    public void setNewCommunityGoal(CommunityGoalEntity newCommunityGoal) {
        this.newCommunityGoal = newCommunityGoal;
    }

    public CommunityGoalEntity getSelectedCommunityGoal() {
        return selectedCommunityGoal;
    }

    public void setSelectedCommunityGoal(CommunityGoalEntity selectedCommunityGoal) {
        this.selectedCommunityGoal = selectedCommunityGoal;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public Date getAfterStartDate() {
        return afterStartDate;
    }

    public void setAfterStartDate(Date afterStartDate) {
        this.afterStartDate = afterStartDate;
    }
}
