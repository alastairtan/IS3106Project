/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.stateless.ReviewEntityControllerLocal;
import entity.ReviewEntity;
import entity.StaffEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.NoResultException;
import util.exception.DeleteReviewException;
import util.exception.InputDataValidationException;
import util.exception.ReviewNotFoundException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author shawn
 */
@Named(value = "reviewChainManagedBean")
@ViewScoped
public class reviewChainManagedBean implements Serializable {

    @EJB(name = "ReviewEntityControllerLocal")
    private ReviewEntityControllerLocal reviewEntityControllerLocal;

    private List<ReviewEntity> reviewChain;

    private ReviewEntity rootReview;

    private ReviewEntity selectedReview;

    private ReviewEntity reviewToDelete;

    private String reviewReply;

    public reviewChainManagedBean() {
        reviewChain = new ArrayList<>();
    }

    @PostConstruct
    public void PostConstruct() {
        Long rootReviewId = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("rootReviewId"));
        try {
            this.rootReview = reviewEntityControllerLocal.retrieveReviewByReviewId(rootReviewId);
            this.reviewChain = reviewEntityControllerLocal.getReviewChain(rootReviewId);
        } catch (ReviewNotFoundException ex) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("reviewManagement.xhtml");
            } catch (IOException ex1) {
                Logger.getLogger(reviewChainManagedBean.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void submitReply(ActionEvent event) {
        try {
            ReviewEntity reply = new ReviewEntity(reviewReply, new Date());
            StaffEntity s = (StaffEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentStaffEntity");
            reviewEntityControllerLocal.replyReview(selectedReview.getReviewId(), reply, s.getStaffId());
            this.reviewChain = reviewEntityControllerLocal.getReviewChain(rootReview.getReviewId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reply Submitted", null));
        } catch (InputDataValidationException | StaffNotFoundException | ReviewNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR: " + ex.getMessage(), null));
        }

    }

    public void editReply(ActionEvent event) {
        try {
            reviewEntityControllerLocal.updateReview(selectedReview.getReviewId(), selectedReview.getContent(), null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reply Edited", null));
        } catch (ReviewNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occured during edit: " + ex.getMessage(), null));
        }
    }

    public void deleteReview(ActionEvent event) {
        try {
            Long reviewToDeleteId = (Long) event.getComponent().getAttributes().get("reviewToDeleteId");
            reviewEntityControllerLocal.deleteReview(reviewToDeleteId);
            try {
                this.reviewChain = reviewEntityControllerLocal.getReviewChain(rootReview.getReviewId());
            } catch (ReviewNotFoundException ex) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("reviewManagement.xhtml");
                } catch (IOException ex1) {
                    Logger.getLogger(reviewChainManagedBean.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Delete Successful", null));
        } catch (DeleteReviewException | NoResultException | ReviewNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error occured during deletion: " + ex.getMessage(), null));
        }
    }

    public List<ReviewEntity> getReviewChain() {
        return reviewChain;
    }

    public void setReviewChain(List<ReviewEntity> reviewChain) {
        this.reviewChain = reviewChain;
    }

    public ReviewEntity getRootReview() {
        return rootReview;
    }

    public void setRootReview(ReviewEntity rootReview) {
        this.rootReview = rootReview;
    }

    public ReviewEntity getSelectedReview() {
        return selectedReview;
    }

    public void setSelectedReview(ReviewEntity selectedReview) {
        this.selectedReview = selectedReview;
    }

    public String getReviewReply() {
        return reviewReply;
    }

    public void setReviewReply(String reviewReply) {
        this.reviewReply = reviewReply;
    }

    public ReviewEntity getReviewToDelete() {
        return reviewToDelete;
    }

    public void setReviewToDelete(ReviewEntity reviewToDelete) {
        this.reviewToDelete = reviewToDelete;
    }

}
