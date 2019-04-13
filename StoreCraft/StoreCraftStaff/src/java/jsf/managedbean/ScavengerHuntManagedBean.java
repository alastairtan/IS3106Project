/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.ProductEntityControllerLocal;
import ejb.stateless.ScavengerHuntEntityControllerLocal;
import entity.ProductEntity;
import entity.ScavengerHuntEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.CloseEvent;
import util.enumeration.RewardTypeEnum;
import util.exception.InputDataValidationException;
import util.exception.ScavengerHuntAlreadyExistException;
import util.exception.ScavengerHuntNotFoundException;

/**
 *
 * @author bryan
 */
@Named(value = "scavengerHuntManagedBean")
@ViewScoped
public class ScavengerHuntManagedBean implements Serializable {

    @EJB
    private ScavengerHuntEntityControllerLocal scavengerHuntEntityControllerLocal;

    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;

    private List<ScavengerHuntEntity> scavengers;

    private List<ScavengerHuntEntity> filteredScavengers;

    private ScavengerHuntEntity selectedScavengerHunt;

    private ScavengerHuntEntity currentScavengerHunt;

    private List<ProductEntity> currentScavengerHuntProducts;
    
    private Boolean isUpdating;
    
    private RewardTypeEnum[] rewardTypeEnums;

    public ScavengerHuntManagedBean() {
        rewardTypeEnums = RewardTypeEnum.values();
    }

    @PostConstruct
    public void postConstruct() {
        try {
            this.currentScavengerHunt = scavengerHuntEntityControllerLocal.retrieveScavengerHuntEntityByDate(new Date());
            this.scavengers = scavengerHuntEntityControllerLocal.retrieveAllScavengerHunts();
            this.currentScavengerHuntProducts = productEntityControllerLocal.retrieveAllScavengerHuntProducts();
        } catch (ScavengerHuntNotFoundException ex) {
            this.currentScavengerHunt = null;
            this.scavengers = scavengerHuntEntityControllerLocal.retrieveAllScavengerHunts();
            if (currentScavengerHunt != null) {
                this.scavengers.remove(currentScavengerHunt);
            }
            this.currentScavengerHuntProducts = productEntityControllerLocal.retrieveAllScavengerHuntProducts();
        }
    }

    public void createNewScavengerHunt(ActionEvent event) {
        try {
            scavengerHuntEntityControllerLocal.createScavengerHuntEntity();
            refresh();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Scavenger Hunt (ID: " + currentScavengerHunt.getScavengerHuntId() + ") Successfully Created ", null));
        } catch (ScavengerHuntAlreadyExistException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Only One Scavenger Hunt Allowed Per Day!", null));
        }
    }

    public void removeCurrentScavengerHunt(ActionEvent event) {
        try {
            scavengerHuntEntityControllerLocal.removeScavengerHuntEntity(currentScavengerHunt.getScavengerHuntId());
            refresh();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Scavenger Hunt Successfully Deleted ", null));
        } catch (ScavengerHuntNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Deleting Scavenger Hunt: " + ex.getMessage(), null));
        }
    }

    public void refresh(){
        try {
            this.currentScavengerHunt = scavengerHuntEntityControllerLocal.retrieveScavengerHuntEntityByDate(new Date());
            this.scavengers = scavengerHuntEntityControllerLocal.retrieveAllScavengerHunts();
            this.currentScavengerHuntProducts = productEntityControllerLocal.retrieveAllScavengerHuntProducts();
        } catch (ScavengerHuntNotFoundException ex){
            this.currentScavengerHunt = null;
            this.scavengers = scavengerHuntEntityControllerLocal.retrieveAllScavengerHunts();
            if (currentScavengerHunt != null) {
                this.scavengers.remove(currentScavengerHunt);
            }
            this.currentScavengerHuntProducts = productEntityControllerLocal.retrieveAllScavengerHuntProducts();
        }
    }
    
    public void refreshWinners(ActionEvent event) {
        refresh();
        FacesContext.getCurrentInstance().addMessage("refreshBtn", new FacesMessage(FacesMessage.SEVERITY_INFO, "Refreshed", null));
    }
    
    public void updating (ActionEvent event){
        setIsUpdating(true);
    }
    
    public void cancelUpdating (ActionEvent event){
        setIsUpdating(false);
        try {
            this.currentScavengerHunt = scavengerHuntEntityControllerLocal.retrieveScavengerHuntEntityById(currentScavengerHunt.getScavengerHuntId());
        } catch(ScavengerHuntNotFoundException ex){
            ex.printStackTrace(); //ignore exception
        }
    } 
    
    public void saveChanges (ActionEvent event){
        try {
            scavengerHuntEntityControllerLocal.updateScavengerHuntEntity(currentScavengerHunt);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Scavenger Hunt (ID: " + currentScavengerHunt.getScavengerHuntId() + ") Successfully Updated ", null));
        } catch (InputDataValidationException | ScavengerHuntNotFoundException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Updating Scavenger Hunt: " + ex.getMessage(), null));
        }
        cancelUpdating(null);
    }
    

    public void closeViewCustomerDialog(CloseEvent event) {
        selectedScavengerHunt = new ScavengerHuntEntity();
    }

    public ScavengerHuntEntityControllerLocal getScavengerHuntEntityControllerLocal() {
        return scavengerHuntEntityControllerLocal;
    }

    public void setScavengerHuntEntityControllerLocal(ScavengerHuntEntityControllerLocal scavengerHuntEntityControllerLocal) {
        this.scavengerHuntEntityControllerLocal = scavengerHuntEntityControllerLocal;
    }

    public List<ScavengerHuntEntity> getScavengers() {
        return scavengers;
    }

    public void setScavengers(List<ScavengerHuntEntity> scavengers) {
        this.scavengers = scavengers;
    }

    public List<ScavengerHuntEntity> getFilteredScavengers() {
        return filteredScavengers;
    }

    public void setFilteredScavengers(List<ScavengerHuntEntity> filteredScavengers) {
        this.filteredScavengers = filteredScavengers;
    }

    public ScavengerHuntEntity getSelectedScavengerHunt() {
        return selectedScavengerHunt;
    }

    public void setSelectedScavengerHunt(ScavengerHuntEntity selectedScavengerHunt) {
        this.selectedScavengerHunt = selectedScavengerHunt;
    }

    public ScavengerHuntEntity getCurrentScavengerHunt() {
        return currentScavengerHunt;
    }

    public void setCurrentScavengerHunt(ScavengerHuntEntity currentScavengerHunt) {
        this.currentScavengerHunt = currentScavengerHunt;
    }

    public List<ProductEntity> getCurrentScavengerHuntProducts() {
        return currentScavengerHuntProducts;
    }

    public void setCurrentScavengerHuntProducts(List<ProductEntity> currentScavengerHuntProducts) {
        this.currentScavengerHuntProducts = currentScavengerHuntProducts;
    }

    public Boolean getIsUpdating() {
        return isUpdating;
    }

    public void setIsUpdating(Boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    public RewardTypeEnum[] getRewardTypeEnums() {
        return rewardTypeEnums;
    }

    public void setRewardTypeEnums(RewardTypeEnum[] rewardTypeEnums) {
        this.rewardTypeEnums = rewardTypeEnums;
    }

}
