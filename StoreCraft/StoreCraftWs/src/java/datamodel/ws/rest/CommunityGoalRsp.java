/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.CommunityGoalEntity;
import java.util.List;

/**
 *
 * @author Win Phong
 */
public class CommunityGoalRsp {

    private List<CommunityGoalEntity> communityGoalEntities;
    private CommunityGoalEntity communityGoalEntity;
    
    
    // Constructtors 
    public CommunityGoalRsp() {
    }
    
    public CommunityGoalRsp(List<CommunityGoalEntity> communityGoalEntities) 
    {
        this.communityGoalEntities = communityGoalEntities;
    }
    
    public CommunityGoalRsp(CommunityGoalEntity communityGoalEntity) 
    {
        this.communityGoalEntity = communityGoalEntity;
    }
    
    

    public List<CommunityGoalEntity> getCommunityGoalEntities() {
        return communityGoalEntities;
    }

    public void setCommunityGoalEntities(List<CommunityGoalEntity> communityGoalEntities) {
        this.communityGoalEntities = communityGoalEntities;
    }

    public CommunityGoalEntity getCommunityGoalEntity() {
        return communityGoalEntity;
    }

    public void setCommunityGoalEntity(CommunityGoalEntity communityGoalEntity) {
        this.communityGoalEntity = communityGoalEntity;
    }
    
    
}
