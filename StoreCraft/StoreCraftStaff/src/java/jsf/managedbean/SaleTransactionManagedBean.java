/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.SaleTransactionEntityControllerLocal;
import entity.SaleTransactionEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.SaleTransactionNotFoundException;

/**
 *
 * @author Win Phong
 */
@Named
@ViewScoped
public class SaleTransactionManagedBean implements Serializable {

    @EJB
    private SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal;
    
    private List<SaleTransactionEntity> saleTransactionEntities;
    private List<SaleTransactionEntity> filteredSaleTransactionEntities;
    private List<SaleTransactionEntity> filteredSaleTransactionLineItems;
    
    private SaleTransactionEntity selectedSaleTransactionEntity;
    
    public SaleTransactionManagedBean() {
        
    }
    
    @PostConstruct
    public void postConstruct()
    {
        saleTransactionEntities = saleTransactionEntityControllerLocal.retrieveAllSaleTransactions();
    }
    
//    public void viewLineItems(ActionEvent event) throws IOException
//    {
//        Long saleTransactionLineItemIdToView = (Long)event.getComponent().getAttributes().get("saleTransactionEntityToView");
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("saleTransactionEntityToView", saleTransactionLineItemIdToView);
//        FacesContext.getCurrentInstance().getExternalContext().redirect("viewSaleTransactionLineItems.xhtml");
//        
//    }
    
    public void deleteSaleTransaction(ActionEvent event) 
    {   
        try
        {
            SaleTransactionEntity saleTransactionToDelete = (SaleTransactionEntity) event.getComponent().getAttributes().get("saleTransactionToDelete");
            saleTransactionEntityControllerLocal.deleteSaleTransaction(saleTransactionToDelete.getSaleTransactionId());

            saleTransactionEntities.remove(saleTransactionToDelete);
            System.out.println("Removing in the managed bean");

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction deleted successfully", null));
        } 
        catch (SaleTransactionNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting product: " + ex.getMessage(), null));
        }
        catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    // Getter & Setter

    public List<SaleTransactionEntity> getSaleTransactionEntities() {
        return saleTransactionEntities;
    }

    public void setSaleTransactionEntities(List<SaleTransactionEntity> saleTransactionEntities) {
        this.saleTransactionEntities = saleTransactionEntities;
    }

    public List<SaleTransactionEntity> getFilteredSaleTransactionEntities() {
        return filteredSaleTransactionEntities;
    }

    public void setFilteredSaleTransactionEntities(List<SaleTransactionEntity> filteredSaleTransactionEntities) {
        this.filteredSaleTransactionEntities = filteredSaleTransactionEntities;
    }

    public SaleTransactionEntity getSelectedSaleTransactionEntity() {
        return selectedSaleTransactionEntity;
    }

    public void setSelectedSaleTransactionEntity(SaleTransactionEntity selectedSaleTransactionEntity) {
        this.selectedSaleTransactionEntity = selectedSaleTransactionEntity;
    }
    
    public List<SaleTransactionEntity> getFilteredSaleTransactionLineItems() {
        return filteredSaleTransactionLineItems;
    }

    public void setFilteredSaleTransactionLineItems(List<SaleTransactionEntity> filteredSaleTransactionLineItems) {
        this.filteredSaleTransactionLineItems = filteredSaleTransactionLineItems;
    }
    
}
