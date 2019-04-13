/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import util.enumeration.RewardTypeEnum;

/**
 *
 * @author shawn
 */
@Entity
public class ScavengerHuntEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scavengerHuntId;
   
    private RewardTypeEnum rewardTypeEnum;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @NotNull
    private Date scavengerHuntDate;
    
    @Min(1)
    @NotNull
    private Integer numWinnersRemaining;
       
    //RELATIONSHIPS
        
    @OneToMany //unidirectional
    private List<CustomerEntity> customerEntities; 

    public ScavengerHuntEntity() {
        customerEntities = new ArrayList();
    }

    public ScavengerHuntEntity(Date scavengerHuntDate, Integer numWinnersRemaining) {
        this();
        this.scavengerHuntDate = scavengerHuntDate;     
        this.numWinnersRemaining = numWinnersRemaining;
        int index = (int) (Math.random() * 3);
        this.rewardTypeEnum = RewardTypeEnum.values()[index]; //randomly select reward type 
    }

    public Long getScavengerHuntId() {
        return scavengerHuntId;
    }

    public void setScavengerHuntId(Long scavengerHuntId) {
        this.scavengerHuntId = scavengerHuntId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scavengerHuntId != null ? scavengerHuntId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the scavengerHuntId fields are not set
        if (!(object instanceof ScavengerHuntEntity)) {
            return false;
        }
        ScavengerHuntEntity other = (ScavengerHuntEntity) object;
        if ((this.scavengerHuntId == null && other.scavengerHuntId != null) || (this.scavengerHuntId != null && !this.scavengerHuntId.equals(other.scavengerHuntId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ScavengerHuntEntity[ id=" + scavengerHuntId + " ]";
    }

    public RewardTypeEnum getRewardTypeEnum() {
        return rewardTypeEnum;
    }

    public void setRewardTypeEnum(RewardTypeEnum rewardTypeEnum) {
        this.rewardTypeEnum = rewardTypeEnum;
    }

    public Date getScavengerHuntDate() {
        return scavengerHuntDate;
    }

    public void setScavengerHuntDate(Date scavengerHuntDate) {
        this.scavengerHuntDate = scavengerHuntDate;
    }

    public Integer getNumWinnersRemaining() {
        return numWinnersRemaining;
    }

    public void setNumWinnersRemaining(Integer numWinnersRemaining) {
        this.numWinnersRemaining = numWinnersRemaining;
    }

    public List<CustomerEntity> getCustomerEntities() {
        return customerEntities;
    }

    public void setCustomerEntities(List<CustomerEntity> customerEntities) {
        this.customerEntities = customerEntities;
    }

    
}
