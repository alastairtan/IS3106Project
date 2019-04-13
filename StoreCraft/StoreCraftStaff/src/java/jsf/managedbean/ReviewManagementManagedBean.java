/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.ReviewEntityControllerLocal;
import entity.ReviewEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author shawn
 */
@Named(value = "reviewManagementManagedBean")
@ViewScoped
public class ReviewManagementManagedBean implements Serializable{

    @EJB(name = "ReviewEntityControllerLocal")
    private ReviewEntityControllerLocal reviewEntityControllerLocal;
    
    private List<ReviewEntity> outstandingCustomerReviews;
    
    private List<ReviewEntity> allRootReviewEntities;
    
    private List<ReviewEntity> filteredRootReviewEntities;

    
    public ReviewManagementManagedBean() {
        outstandingCustomerReviews = new ArrayList<>();
    }
    
    @PostConstruct
    public void PostConstruct(){
        this.outstandingCustomerReviews = reviewEntityControllerLocal.getOutstandingCustomerReviews();
        this.allRootReviewEntities = reviewEntityControllerLocal.retrieveAllRootReviewEntities();
    }

    public List<ReviewEntity> getOutstandingCustomerReviews() {
        return outstandingCustomerReviews;
    }

    public void setOutstandingCustomerReviews(List<ReviewEntity> outstandingCustomerReviews) {
        this.outstandingCustomerReviews = outstandingCustomerReviews;
    }

    public List<ReviewEntity> getAllRootReviewEntities() {
        return allRootReviewEntities;
    }

    public void setAllRootReviewEntities(List<ReviewEntity> allRootReviewEntities) {
        this.allRootReviewEntities = allRootReviewEntities;
    }

    public List<ReviewEntity> getFilteredRootReviewEntities() {
        return filteredRootReviewEntities;
    }

    public void setFilteredRootReviewEntities(List<ReviewEntity> filteredRootReviewEntities) {
        this.filteredRootReviewEntities = filteredRootReviewEntities;
    }
    
}
