/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CommunityGoalEntity;
import entity.DiscountCodeEntity;
import entity.StaffEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CommunityGoalNotFoundException;
import util.exception.CreateNewCommunityGoalException;
import util.exception.CreateNewDiscountCodeException;
import util.exception.DiscountCodeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author shawn
 */
@Stateless
@Local (CommunityGoalEntityControllerLocal.class)
public class CommunityGoalEntityController implements CommunityGoalEntityControllerLocal {

    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;

    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    @PersistenceContext(unitName = "StoreCraft-ejbPU")
    private EntityManager entityManager;
    
    public CommunityGoalEntityController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public CommunityGoalEntity createNewCommunityGoal(CommunityGoalEntity communityGoalEntity,Long staffId) throws InputDataValidationException, StaffNotFoundException, CreateNewCommunityGoalException
    {        
        Set<ConstraintViolation<CommunityGoalEntity>>constraintViolations = validator.validate(communityGoalEntity);
        
        if(constraintViolations.isEmpty())
        {
            try{
            StaffEntity staffEntity = staffEntityControllerLocal.retrieveStaffByStaffId(staffId);
            entityManager.persist(communityGoalEntity);
            entityManager.flush();

            return communityGoalEntity;
            } catch(PersistenceException ex)
            {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                {
                    throw new CreateNewCommunityGoalException("Community Goal already exists");
                }
                else
                {
                    throw new CreateNewCommunityGoalException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<CommunityGoalEntity> retrieveAllCommunityGoals() {
        Query query = entityManager.createQuery("SELECT dc FROM CommunityGoalEntity dc ORDER BY dc.communityGoalId ASC");
        List<CommunityGoalEntity> communityGoalEntities = query.getResultList();
        
        for(CommunityGoalEntity communityGoalEntity:communityGoalEntities)
        {
            communityGoalEntity.getStaffEntity();
        }
        
        return communityGoalEntities;
    }
    
    @Override
    public CommunityGoalEntity retrieveCommunityGoalByCommunityGoalId(Long communityGoalId) throws CommunityGoalNotFoundException{
        if(communityGoalId == null) {
            throw new CommunityGoalNotFoundException("Community Goal ID is not provided");
        }
        
        CommunityGoalEntity communityGoalEntity = entityManager.find(CommunityGoalEntity.class, communityGoalId);
        
        if(communityGoalEntity == null) {
            throw new CommunityGoalNotFoundException("Community Goal " + communityGoalId + "does not exist");
        } else {
            communityGoalEntity.getStaffEntity();
            
            return communityGoalEntity;
        }
    }
    
    @Override
    public void deleteCommunityGoal(Long communityGoalId) throws CommunityGoalNotFoundException{
        CommunityGoalEntity communityGoalEntity = retrieveCommunityGoalByCommunityGoalId(communityGoalId);
        
        communityGoalEntity.getStaffEntity().getCommunityGoalEntities().remove(communityGoalEntity);
        
        communityGoalEntity.setStaffEntity(null);
        
        entityManager.remove(communityGoalEntity);
    }
    
    @Override
    public void updateCommunityGoal(CommunityGoalEntity newCommunityGoalEntity, Long communityGoalId) throws InputDataValidationException, CommunityGoalNotFoundException{
        
        Set<ConstraintViolation<CommunityGoalEntity>>constraintViolations = validator.validate(newCommunityGoalEntity);
        
        CommunityGoalEntity communityGoalEntity = retrieveCommunityGoalByCommunityGoalId(communityGoalId);
        
        if(constraintViolations.isEmpty())
        {
            communityGoalEntity.setCountry(newCommunityGoalEntity.getCountry());
            communityGoalEntity.setDescription(newCommunityGoalEntity.getDescription());
            communityGoalEntity.setEndDate(newCommunityGoalEntity.getEndDate());
            communityGoalEntity.setStartDate(newCommunityGoalEntity.getStartDate());
            communityGoalEntity.setGoalTitle(newCommunityGoalEntity.getGoalTitle());
            communityGoalEntity.setTargetPoints(newCommunityGoalEntity.getTargetPoints());
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CommunityGoalEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

}
