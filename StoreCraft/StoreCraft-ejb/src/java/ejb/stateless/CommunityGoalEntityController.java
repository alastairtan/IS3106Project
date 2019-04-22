/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CommunityGoalEntity;
import entity.CustomerEntity;
import entity.DiscountCodeEntity;
import entity.StaffEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import util.enumeration.DiscountCodeTypeEnum;
import util.exception.CommunityGoalNotFoundException;
import util.exception.CreateNewCommunityGoalException;
import util.exception.CreateNewDiscountCodeException;
import util.exception.DateNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author shawn
 */
@Stateless
@Local (CommunityGoalEntityControllerLocal.class)
public class CommunityGoalEntityController implements CommunityGoalEntityControllerLocal {

    @EJB(name = "DiscountCodeEntityControllerLocal")
    private DiscountCodeEntityControllerLocal discountCodeEntityControllerLocal;

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

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


            staffEntity.getCommunityGoalEntities().add(communityGoalEntity);
            communityGoalEntity.setStaffEntity(staffEntity);

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
            communityGoalEntity.setTargetPoints(newCommunityGoalEntity.getTargetPoints());
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<CommunityGoalEntity> retrieveCurrentCommunityGoal(String country){
        Date currentDate = new Date();
        
        List<CommunityGoalEntity> communityGoalEntitys= retrieveAllCommunityGoals();
        List<CommunityGoalEntity> currentCommunityGoalEntitys = new LinkedList<>();
        
        for(CommunityGoalEntity cge : communityGoalEntitys) {
            if(cge.isCompleted()) {
                continue;
            }
            if(cge.getCountry().equals(country) && cge.getStartDate().before(currentDate) && cge.getEndDate().after(currentDate)) {
                currentCommunityGoalEntitys.add(cge);
            }
        }
        return currentCommunityGoalEntitys;
        
    }
    
    @Override
    public void addPointsToCommunityGoal(String country, BigDecimal points){
        
        List<CommunityGoalEntity> communityGoals = retrieveCurrentCommunityGoal(country);
        
        //Retreive Customers by Country
        Query query = entityManager.createQuery("SELECT c FROM CustomerEntity c WHERE c.country = :inCountry");
        query.setParameter("inCountry", country);
        List<CustomerEntity> customers = query.getResultList();
        
        if(!communityGoals.isEmpty()){
            
            //Set 3 months expiry limit for discount code
            Date startDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            c.add(Calendar.MONTH, 3);
            Date endDate = c.getTime();
            
            //Give Discount Code
            for(CommunityGoalEntity cge : communityGoals){
                BigDecimal addingPoints = cge.getCurrentPoints().add(points);
                if(addingPoints.compareTo(cge.getTargetPoints()) >= 0 && !cge.isCompleted()){
                    cge.setCompleted(true);
                    cge.setCurrentPoints(addingPoints);
                    DiscountCodeEntity dce = new DiscountCodeEntity(startDate, endDate,customers.size(), "CG" + cge.getCommunityGoalId(), DiscountCodeTypeEnum.PERCENTAGE, cge.getRewardPercentage());
                    List<Long> customerEntityIds = new ArrayList<>();
                    List<Long> productEntityIds = new ArrayList<>();
                    
                    for(CustomerEntity ce : customers){
                        customerEntityIds.add(ce.getCustomerId());
                    }
                    try {
                        discountCodeEntityControllerLocal.createNewDiscountCode(dce, customerEntityIds, productEntityIds);
                    } catch (CreateNewDiscountCodeException |InputDataValidationException ex) {
                        System.err.println(ex.getMessage());
                    }
                      
                }else {
                    cge.setCurrentPoints(addingPoints);
                }
            }
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
