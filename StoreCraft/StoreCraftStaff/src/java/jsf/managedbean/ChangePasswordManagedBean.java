/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.StaffEntityControllerLocal;
import entity.StaffEntity;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author shawn
 */
@Named(value = "changePasswordManagedBean")
@RequestScoped
public class ChangePasswordManagedBean {

    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;
    
    private String oldPassword;

    private String newPassword1;
    
    private String newPassword2;
    
    public ChangePasswordManagedBean() {
    }
    
    public void changePassword(ActionEvent event){
        if (!newPassword1.equals(newPassword2)){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "New Passwords Do Not Match!", null));
            return;
        }
        StaffEntity currentStaffEntity = (StaffEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStaffEntity");
        if (currentStaffEntity == null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not Logged In!", null));
            return;
        }
        try {
            staffEntityControllerLocal.updatePassword(currentStaffEntity, oldPassword, newPassword2); //covers old pw incorrect
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Changed Successfully!", null));
        } catch (StaffNotFoundException | InvalidLoginCredentialException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot change password: " + ex.getMessage(), null));
        }
    }

    /**
     * @return the oldPassword
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * @param oldPassword the oldPassword to set
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword1() {
        return newPassword1;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword1(String newPassword) {
        this.newPassword1 = newPassword;
    }
    
    public String getNewPassword2() {
        return newPassword2;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword2(String newPassword) {
        this.newPassword2 = newPassword;
    }
    
}
