/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 *
 * @author shawn
 */
@Entity
public class DiscountCodePercentageEntity extends DiscountCodeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @DecimalMin("0.00")
    @NotNull
    private BigDecimal discountRate;

    public DiscountCodePercentageEntity() {
    }
    
    public DiscountCodePercentageEntity(Date startDate, Date endDate, String discountCode, Integer numAvailable, BigDecimal discountRate) {
        //these 4 from superclass
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountCode = discountCode;
        this.numAvailable = numAvailable;
        
        this.discountRate = discountRate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (discountCodeId != null ? discountCodeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiscountCodePercentageEntity)) {
            return false;
        }
        DiscountCodePercentageEntity other = (DiscountCodePercentageEntity) object;
        if ((this.discountCodeId == null && other.discountCodeId != null) || (this.discountCodeId != null && !this.discountCodeId.equals(other.discountCodeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DiscountCodePercentageEntity[ discountCodeId=" + discountCodeId + " ]";
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }
    
}
