/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.ScavengerHuntEntityControllerLocal;
import entity.ScavengerHuntEntity;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author bryan
 */
@Named(value = "scavengerHuntManagedBean")
@ViewScoped
public class ScavengerHuntManagedBean implements Serializable {

    @EJB
    private ScavengerHuntEntityControllerLocal scavengerHuntEntityControllerLocal;

    private List<ScavengerHuntEntity> scavengers;

    private List<ScavengerHuntEntity> filteredScavengers;

    private ScavengerHuntEntity selectedScavengerHunt;

    private ScavengerHuntEntity currentScavengerHunt;

    public ScavengerHuntManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        scavengers = scavengerHuntEntityControllerLocal.retrieveAllScavengerHunts();
        Date currentDate = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        for (ScavengerHuntEntity s : scavengers) {
            if (fmt.format(currentDate).equals(fmt.format(s.getScavengerHuntDate()))) {
                currentScavengerHunt = s;
            }
        }
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

}
