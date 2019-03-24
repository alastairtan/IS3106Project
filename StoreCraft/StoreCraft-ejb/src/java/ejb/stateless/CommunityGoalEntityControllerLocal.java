/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CommunityGoalEntity;
import java.util.List;
import util.exception.CommunityGoalNotFoundException;
import util.exception.CreateNewCommunityGoalException;
import util.exception.InputDataValidationException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author shawn
 */
public interface CommunityGoalEntityControllerLocal {

    public CommunityGoalEntity createNewCommunityGoal(CommunityGoalEntity communityGoalEntity, Long staffId) throws InputDataValidationException, StaffNotFoundException, CreateNewCommunityGoalException;

    public List<CommunityGoalEntity> retrieveAllCommunityGoals();

    public CommunityGoalEntity retrieveCommunityGoalByCommunityGoalId(Long communityGoalId) throws CommunityGoalNotFoundException;

    public void deleteCommunityGoal(Long communityGoalId) throws CommunityGoalNotFoundException;

    public void updateCommunityGoal(CommunityGoalEntity newCommunityGoalEntity, Long communityGoalId) throws InputDataValidationException, CommunityGoalNotFoundException;
    
}
