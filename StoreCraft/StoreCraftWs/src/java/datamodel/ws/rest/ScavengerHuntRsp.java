/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.ScavengerHuntEntity;
import java.util.List;

/**
 *
 * @author Alastair
 */
public class ScavengerHuntRsp {
    
    private ScavengerHuntEntity scavengerHuntEntity;

    public ScavengerHuntRsp() {
    }

    public ScavengerHuntRsp(ScavengerHuntEntity scavengerHuntEntity) {
        this.scavengerHuntEntity = scavengerHuntEntity;
    }

    public ScavengerHuntEntity getScavengerHuntEntity() {
        return scavengerHuntEntity;
    }

    public void setScavengerHuntEntity(ScavengerHuntEntity scavengerHuntEntity) {
        this.scavengerHuntEntity = scavengerHuntEntity;
    }
    
}
