/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.ScavengerHuntEntity;

/**
 *
 * @author Win Phong
 */
public class ScavengerHuntRsp {
    
    private Boolean hasCustomerWonToday;
    private ScavengerHuntEntity scavengerHuntEntity;
    
    public ScavengerHuntRsp() {
    }

    public ScavengerHuntRsp(Boolean hasWonToday) {
        this.hasCustomerWonToday = hasWonToday;
    }

    public ScavengerHuntRsp(ScavengerHuntEntity scavengerHuntEntity) {
        this.scavengerHuntEntity = scavengerHuntEntity;
    }
    
    public Boolean getHasCustomerWonToday() {
        return hasCustomerWonToday;
    }

    public void setHasCustomerWonToday(Boolean hasCustomerWonToday) {
        this.hasCustomerWonToday = hasCustomerWonToday;
    }

    public ScavengerHuntEntity getScavengerHuntEntity() {
        return scavengerHuntEntity;
    }

    public void setScavengerHuntEntity(ScavengerHuntEntity scavengerHuntEntity) {
        this.scavengerHuntEntity = scavengerHuntEntity;
    }
    
}
