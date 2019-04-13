/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 *
 * @author shawn
 */
@Entity
public class ReviewEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    
    private String content;
    
    @Column(nullable = true)
    @Positive
    @Min(1)
    @Max(5)
    private Integer productRating; 
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date reviewDate; 
    
    //RELATIONSHIPS
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private CustomerEntity customerEntity;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private ProductEntity productEntity;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private StaffEntity staffEntity;
    
    @OneToOne(mappedBy = "parentReviewEntity")
    private ReviewEntity replyReviewEntity;
    
    @OneToOne
    private ReviewEntity parentReviewEntity;

    public ReviewEntity() {
    }

    public ReviewEntity(String content, Integer productRating, Date reviewDate) {
        this.content = content;
        this.productRating = productRating;
        this.reviewDate = reviewDate;
    }

    public ReviewEntity(String content, Date reviewDate) {
        this.content = content;
        this.reviewDate = reviewDate;
    }
    
    
    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reviewId != null ? reviewId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reviewId fields are not set
        if (!(object instanceof ReviewEntity)) {
            return false;
        }
        ReviewEntity other = (ReviewEntity) object;
        if ((this.reviewId == null && other.reviewId != null) || (this.reviewId != null && !this.reviewId.equals(other.reviewId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReviewEntity[ id=" + reviewId + " ]";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getProductRating() {
        return productRating;
    }

    public void setProductRating(Integer productRating) {
        this.productRating = productRating;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    /**
     * @return the reviewDate
     */
    public Date getReviewDate() {
        return reviewDate;
    }

    /**
     * @param reviewDate the reviewDate to set
     */
    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    /**
     * @return the productEntity
     */
    public ProductEntity getProductEntity() {
        return productEntity;
    }

    /**
     * @param productEntity the productEntity to set
     */
    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    public StaffEntity getStaffEntity() {
        return staffEntity;
    }

    public void setStaffEntity(StaffEntity staffEntity) {
        this.staffEntity = staffEntity;
    }


    public ReviewEntity getParentReviewEntity() {
        return parentReviewEntity;
    }

    public void setParentReviewEntity(ReviewEntity parentReviewEntity) {
        this.parentReviewEntity = parentReviewEntity;
    }

    public ReviewEntity getReplyReviewEntity() {
        return replyReviewEntity;
    }

    public void setReplyReviewEntity(ReviewEntity replyReviewEntity) {
        this.replyReviewEntity = replyReviewEntity;
    }
    
}
