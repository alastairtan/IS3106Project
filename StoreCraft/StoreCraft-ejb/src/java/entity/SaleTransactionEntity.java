/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author shawn
 */
@Entity
public class SaleTransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleTransactionId;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer totalLineItem;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer totalQuantity;

    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal totalAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date transactionDateTime;

    @Column(nullable = false)
    @NotNull
    private Boolean voidRefund;

    @Column(nullable = true)
    private Integer pointsToUse;
    //RELATIONSHIPS 

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CustomerEntity customerEntity;

    @OneToMany
    private List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities;

    @OneToOne
    private DiscountCodeEntity discountCodeEntity;

    public SaleTransactionEntity() {
        voidRefund = false;
        saleTransactionLineItemEntities = new ArrayList<>();
    }

    public SaleTransactionEntity(Integer totalLineItem, Integer totalQuantity, BigDecimal totalAmount, 
            Date transactionDateTime, Boolean voidRefund, CustomerEntity customerEntity, 
            List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities, Integer pointsToUse, DiscountCodeEntity discountCodeEntity) {
        this();
        this.totalLineItem = totalLineItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.transactionDateTime = transactionDateTime;
        this.voidRefund = voidRefund;
        this.customerEntity = customerEntity;
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
        this.pointsToUse = pointsToUse;
        this.discountCodeEntity = discountCodeEntity;
    }

    public Long getSaleTransactionId() {
        return saleTransactionId;
    }

    public void setSaleTransactionId(Long saleTransactionId) {
        this.saleTransactionId = saleTransactionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (saleTransactionId != null ? saleTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the saleTransactionId fields are not set
        if (!(object instanceof SaleTransactionEntity)) {
            return false;
        }
        SaleTransactionEntity other = (SaleTransactionEntity) object;
        if ((this.saleTransactionId == null && other.saleTransactionId != null) || (this.saleTransactionId != null && !this.saleTransactionId.equals(other.saleTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SaleTransactionEntity[ id=" + saleTransactionId + " ]";
    }

    public Integer getTotalLineItem() {
        return totalLineItem;
    }

    public void setTotalLineItem(Integer totalLineItem) {
        this.totalLineItem = totalLineItem;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public Boolean getVoidRefund() {
        return voidRefund;
    }

    public void setVoidRefund(Boolean voidRefund) {
        this.voidRefund = voidRefund;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public List<SaleTransactionLineItemEntity> getSaleTransactionLineItemEntities() {
        return saleTransactionLineItemEntities;
    }

    public void setSaleTransactionLineItemEntities(List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities) {
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }

    public Integer getPointsToUse() {
        return pointsToUse;
    }

    public void setPointsToUse(Integer pointsToUse) {
        this.pointsToUse = pointsToUse;
    }

    public DiscountCodeEntity getDiscountCodeEntity() {
        return discountCodeEntity;
    }

    public void setDiscountCodeEntity(DiscountCodeEntity discountCodeEntity) {
        this.discountCodeEntity = discountCodeEntity;
    }

}
