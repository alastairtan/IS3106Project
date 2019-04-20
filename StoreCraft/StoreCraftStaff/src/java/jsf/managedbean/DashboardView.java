/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.CustomerEntityControllerLocal;
import ejb.stateless.EjbTimerSessionBeanLocal;
import ejb.stateless.ProductEntityControllerLocal;
import ejb.stateless.SaleTransactionEntityControllerLocal;
import entity.CustomerEntity;
import entity.ProductEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import util.exception.CreateNewDiscountCodeException;
import util.exception.InputDataValidationException;

/**
 *
 * @author shawn
 */
@Named(value = "dashboardView")
@ViewScoped
public class DashboardView implements Serializable {

    private DashboardModel model;

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    @EJB
    private SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal;

    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;

    @EJB
    private EjbTimerSessionBeanLocal ejbTimerSessionBeanLocal;

    private List<CustomerEntity> topCustomersAllTime;
    private List<CustomerEntity> topCustomersForTheMonth;
    private List<BigDecimal> saleTransactionsForTheYear;
    private List<String> months = new ArrayList<>();
    private List<ProductEntity> topSellingProductsAllTime;
    private List<ProductEntity> topSellingProductsPerMonth;
    private int selectedMonthToDisplay;
    
    private List<ProductEntity> filteredTopSellingProductsPerMonth;

    public DashboardView() {
        this.topCustomersAllTime = new ArrayList<>();
        this.topCustomersForTheMonth = new ArrayList<>();
        this.topSellingProductsAllTime = new ArrayList<>();
        this.topSellingProductsPerMonth = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int currMonth = calendar.get(Calendar.MONTH);
        selectedMonthToDisplay = currMonth;
        
        this.filteredTopSellingProductsPerMonth = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        DashboardColumn column3 = new DefaultDashboardColumn();

        
        column1.addWidget("topSellingProductsAllTime");
        
        column1.addWidget("topCustomersMonth");
         
        column2.addWidget("topSellingProductsMonth");
        column2.addWidget("salesForMonth");
        
        
        column2.addWidget("topCustomersAllTime"); //match the id of panel in the dashboard
        
 

        model.addColumn(column1);
        model.addColumn(column2);
        model.addColumn(column3);

        retrieveDetailsForDashboard();

    }

    public void retrieveDetailsForDashboard() {
        topCustomersAllTime = new ArrayList<CustomerEntity>();
        topCustomersForTheMonth = new ArrayList<CustomerEntity>();
        months.add("Jan");
        months.add("Feb");
        months.add("Mar");
        months.add("Apr");
        months.add("May");
        months.add("Jun");
        months.add("Jul");
        months.add("Aug");
        months.add("Sep");
        months.add("Oct");
        months.add("Nov");
        months.add("Dec");

        List<CustomerEntity> allCustomersAllTime = customerEntityControllerLocal.retrieveCustomersBySpendingTotal();
        topCustomersAllTime = allCustomersAllTime.subList(0, Math.min(allCustomersAllTime.size(), 3));

        List<CustomerEntity> allCustomersPerMonth = customerEntityControllerLocal.retrieveCustomersBySpendingPerMonth();
        topCustomersForTheMonth = allCustomersPerMonth.subList(0, Math.min(allCustomersAllTime.size(), 3));
        System.out.println("topCustomers size" + topCustomersAllTime.size());

        saleTransactionsForTheYear = saleTransactionEntityControllerLocal.retrieveSaleTransactionForTheYear();
        System.out.println("saleTransactionsForTheYear" + saleTransactionsForTheYear.size());

        //topSellingProductsAllTime = saleTransactionEntityControllerLocal.retrieveTopSellingProductsAllTime();
        //System.out.println("topselling products size" + topSellingProductsAllTime.size());
        topSellingProductsAllTime = productEntityControllerLocal.retrieveAllProducts();
        sortProducts(topSellingProductsAllTime);
        topSellingProductsPerMonth = saleTransactionEntityControllerLocal.retrieveSaleTransactionsPerMonth(selectedMonthToDisplay);
        sortProducts(topSellingProductsPerMonth);
    }

    public void handleReorder(DashboardReorderEvent event) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("Reordered: " + event.getWidgetId());
        message.setDetail("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: " + event.getSenderColumnIndex());

        addMessage(message);
    }

    public void sortProducts(List<ProductEntity> allProducts) {
        Collections.sort(allProducts, new Comparator<ProductEntity>() {
            public int compare(ProductEntity pe1, ProductEntity pe2) {
                int pe1Qty = saleTransactionEntityControllerLocal.numberOfProductsSold(pe1.getProductId());
                int pe2Qty = saleTransactionEntityControllerLocal.numberOfProductsSold(pe2.getProductId());
                return pe1Qty < pe2Qty ? 1 : pe1Qty > pe2Qty ? -1 : 0;
            }
        });
        
        topSellingProductsAllTime = topSellingProductsAllTime.subList(0, Math.min(topSellingProductsAllTime.size(), 5));
    }
    
    public void sortProductsPerMonth(){
        System.out.println("selectedMonth to display" + selectedMonthToDisplay);
        
        topSellingProductsPerMonth = saleTransactionEntityControllerLocal.retrieveSaleTransactionsPerMonth(selectedMonthToDisplay);
        Collections.sort(topSellingProductsPerMonth, new Comparator<ProductEntity>(){
            public int compare(ProductEntity pe1, ProductEntity pe2){
                int pe1Qty = saleTransactionEntityControllerLocal.numberOfProductsSold(pe1.getProductId());
                int pe2Qty = saleTransactionEntityControllerLocal.numberOfProductsSold(pe2.getProductId());
                return pe1Qty < pe2Qty ? 1 : pe1Qty > pe2Qty ? -1 : 0;
            }
        });
        
        topSellingProductsPerMonth = topSellingProductsPerMonth.subList(0, Math.min(topSellingProductsPerMonth.size(), 5));
    }

    public void giveLeaderBoardPrizes(ActionEvent event) {
        try {
            ejbTimerSessionBeanLocal.giveLeaderBoardPrizes();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully gave prizes!", null));
        } catch (CreateNewDiscountCodeException | InputDataValidationException  ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR: " + ex.getMessage(), null));
        } catch (Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unexpected ERROR: " + ex.getMessage(), null));
        }
                
    }

    public int getNumProductsSold(Long productId) {
        return saleTransactionEntityControllerLocal.numberOfProductsSold(productId);
    }

    public List<CustomerEntity> getTopCustomersAllTime() {
        return topCustomersAllTime;
    }

    public void setTopCustomersAllTime(List<CustomerEntity> topCustomersAllTime) {
        this.topCustomersAllTime = topCustomersAllTime;
    }

    public List<CustomerEntity> getTopCustomersForTheMonth() {
        return topCustomersForTheMonth;
    }

    public void setTopCustomersForTheMonth(List<CustomerEntity> topCustomersForTheMonth) {
        this.topCustomersForTheMonth = topCustomersForTheMonth;
    }

    public void handleClose(CloseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Panel Closed", "Closed panel id:'" + event.getComponent().getId() + "'");

        addMessage(message);
    }

    public void handleToggle(ToggleEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " toggled", "Status:" + event.getVisibility().name());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public DashboardModel getModel() {
        return model;
    }

    public List<BigDecimal> getSaleTransactionsForTheYear() {
        return saleTransactionsForTheYear;
    }

    public void setSaleTransactionsForTheYear(List<BigDecimal> saleTransactionsForTheYear) {
        this.saleTransactionsForTheYear = saleTransactionsForTheYear;
    }

    public List<String> getMonths() {
        return months;
    }

    public void setMonths(List<String> months) {
        this.months = months;
    }

    public List<ProductEntity> getTopSellingProductsAllTime() {
        return topSellingProductsAllTime;
    }

    public void setTopSellingProductsAllTime(List<ProductEntity> topSellingProductsAllTime) {
        this.topSellingProductsAllTime = topSellingProductsAllTime;
    }

    public List<ProductEntity> getTopSellingProductsPerMonth() {
        return topSellingProductsPerMonth;
    }

    public void setTopSellingProductsPerMonth(List<ProductEntity> topSellingProductsPerMonth) {
        this.topSellingProductsPerMonth = topSellingProductsPerMonth;
    }

    public int getSelectedMonthToDisplay() {
        return selectedMonthToDisplay;
    }

    public void setSelectedMonthToDisplay(int selectedMonthToDisplay) {
        this.selectedMonthToDisplay = selectedMonthToDisplay;
    }

    public List<ProductEntity> getFilteredTopSellingProductsPerMonth() {
        return filteredTopSellingProductsPerMonth;
    }

    public void setFilteredTopSellingProductsPerMonth(List<ProductEntity> filteredTopSellingProductsPerMonth) {
        this.filteredTopSellingProductsPerMonth = filteredTopSellingProductsPerMonth;
    }

   
}
