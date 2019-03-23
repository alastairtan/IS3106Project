/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import entity.DiscountCodeEntity;
import entity.DiscountCodeFlatEntity;
import entity.DiscountCodePercentageEntity;
import entity.ProductEntity;
import java.util.Date;
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
import util.exception.CreateNewDiscountCodeException;
import util.exception.CustomerNotFoundException;
import util.exception.DiscountCodeNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author shawn
 */
@Stateless
@Local (DiscountCodeEntityControllerLocal.class)
public class DiscountCodeEntityController implements DiscountCodeEntityControllerLocal {
    
    @PersistenceContext(unitName = "StoreCraft-ejbPU")
    private EntityManager em;
    
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public DiscountCodeEntityController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    public DiscountCodeEntity createNewDiscountCodeFlat(DiscountCodeFlatEntity newDiscountCodeFlatEntity, List<Long> customerEntityIds, List<Long> productEntityIds) throws CreateNewDiscountCodeException, InputDataValidationException {
         Set<ConstraintViolation<DiscountCodeFlatEntity>>constraintViolations = validator.validate(newDiscountCodeFlatEntity);
        
        if(constraintViolations.isEmpty())
        {        
            try
            {   
                if(!customerEntityIds.isEmpty())
                {
                    for(Long customerId:customerEntityIds)
                    {
                        CustomerEntity customerEntity = customerEntityControllerLocal.retrieveCustomerByCustomerId(customerId);
                        newDiscountCodeFlatEntity.addCustomer(customerEntity);
                    }
                }
                
                if(!productEntityIds.isEmpty())
                {
                    for(Long productId:productEntityIds)
                    {
                        ProductEntity productEntity = productEntityControllerLocal.retrieveProductByProductId(productId);
                        newDiscountCodeFlatEntity.addProduct(productEntity);
                    }
                }
                
                em.persist(newDiscountCodeFlatEntity);
                em.flush();

                return newDiscountCodeFlatEntity;
            }
            catch(PersistenceException ex)
            {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                {
                    throw new CreateNewDiscountCodeException("Discount Code already exists");
                }
                else
                {
                    throw new CreateNewDiscountCodeException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
            catch(Exception ex)
            {
                throw new CreateNewDiscountCodeException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessageFlat(constraintViolations));
        }
    }
    
    public DiscountCodeEntity createNewDiscountCodePercentage(DiscountCodePercentageEntity newDiscountCodePercentageEntity, List<Long> customerEntityIds, List<Long> productEntityIds) throws CreateNewDiscountCodeException, InputDataValidationException {
         Set<ConstraintViolation<DiscountCodePercentageEntity>>constraintViolations = validator.validate(newDiscountCodePercentageEntity);
        
        if(constraintViolations.isEmpty())
        {        
            try
            {   
                if(!customerEntityIds.isEmpty())
                {
                    for(Long customerId:customerEntityIds)
                    {
                        CustomerEntity customerEntity = customerEntityControllerLocal.retrieveCustomerByCustomerId(customerId);
                        newDiscountCodePercentageEntity.addCustomer(customerEntity);
                    }
                }
                
                if(!productEntityIds.isEmpty())
                {
                    for(Long productId:productEntityIds)
                    {
                        ProductEntity productEntity = productEntityControllerLocal.retrieveProductByProductId(productId);
                        newDiscountCodePercentageEntity.addProduct(productEntity);
                    }
                }
                
                em.persist(newDiscountCodePercentageEntity);
                em.flush();

                return newDiscountCodePercentageEntity;
            }
            catch(PersistenceException ex)
            {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                {
                    throw new CreateNewDiscountCodeException("Discount Code already exists");
                }
                else
                {
                    throw new CreateNewDiscountCodeException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
            catch(Exception ex)
            {
                throw new CreateNewDiscountCodeException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    public List<DiscountCodeEntity> retrieveAllDiscountCodes() {
        Query query = em.createQuery("SELECT dc FROM DiscountCodeEntity dc ORDER BY dc.discountCodeId ASC");
        List<DiscountCodeEntity> discountCodeEntities = query.getResultList();
        
        for(DiscountCodeEntity discountCodeEntity:discountCodeEntities)
        {
            discountCodeEntity.getCustomerEntities().size();
            discountCodeEntity.getProductEntities().size();
        }
        
        return discountCodeEntities;
    }
    
    public List<DiscountCodeFlatEntity> retrieveAllFlatDiscountCodes() {
        Query query = em.createQuery("SELECT dc FROM DiscountCodeFlatEntity dc ORDER BY dc.discountCodeId ASC");
        List<DiscountCodeFlatEntity> discountCodeFlatEntities = query.getResultList();
        
        for(DiscountCodeEntity discountCodeFlatEntity:discountCodeFlatEntities)
        {
            discountCodeFlatEntity.getCustomerEntities().size();
            discountCodeFlatEntity.getProductEntities().size();
        }
        
        return discountCodeFlatEntities;
    }
    
    public List<DiscountCodePercentageEntity> retrieveAllPercentageDiscountCodes() {
        Query query = em.createQuery("SELECT dc FROM DiscountCodePercentageEntity dc ORDER BY dc.discountCodeId ASC");
        List<DiscountCodePercentageEntity> discountCodePercentageEntities = query.getResultList();
        
        for(DiscountCodeEntity discountCodePercentageEntity:discountCodePercentageEntities)
        {
            discountCodePercentageEntity.getCustomerEntities().size();
            discountCodePercentageEntity.getProductEntities().size();
        }
        
        return discountCodePercentageEntities;
    }
    
    
    public void updateStartDateEndDate(Long discountCodeId,Date newStartDate, Date newEndDate) throws DiscountCodeNotFoundException {
        DiscountCodeEntity discountCodeEntity = retrieveDiscountCodeByDiscountCodeId(discountCodeId);
        discountCodeEntity.setStartDate(newStartDate);
        discountCodeEntity.setEndDate(newEndDate);
        
    }
    
    public void updateAvailable(Long discountCodeId, int numAvailable) throws DiscountCodeNotFoundException {
        DiscountCodeEntity discountCodeEntity = retrieveDiscountCodeByDiscountCodeId(discountCodeId);
        discountCodeEntity.setNumAvailable(numAvailable);
    }
    
    public DiscountCodeEntity retrieveDiscountCodeByDiscountCodeId(Long discountCodeId) throws DiscountCodeNotFoundException {
        if(discountCodeId == null) {
            throw new DiscountCodeNotFoundException("Discount Code ID is not provided");
        }
        
        DiscountCodeEntity discountCodeEntity = em.find(DiscountCodeEntity.class, discountCodeId);
        
        if(discountCodeEntity == null) {
            throw new DiscountCodeNotFoundException("Discount Code " + discountCodeId + "does not exist");
        } else {
            discountCodeEntity.getCustomerEntities().size();
            discountCodeEntity.getProductEntities().size();
            
            return discountCodeEntity;
        }
    }
    
    public void deleteDiscountCode(Long discountCodeId) throws DiscountCodeNotFoundException {
        DiscountCodeEntity discountCodeEntity = retrieveDiscountCodeByDiscountCodeId(discountCodeId);
        
        for(CustomerEntity c:discountCodeEntity.getCustomerEntities()) {
            c.getDiscountCodeEntities().remove(discountCodeEntity);
        }
        
        for(ProductEntity p:discountCodeEntity.getProductEntities()) {
            p.getDiscountCodeEntities().remove(discountCodeEntity);
        }
        
        discountCodeEntity.setCustomerEntities(null);
        discountCodeEntity.setProductEntities(null);
        
        em.remove(discountCodeEntity);
        
        
    }
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<DiscountCodePercentageEntity>> constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    private String prepareInputDataValidationErrorsMessageFlat(Set<ConstraintViolation<DiscountCodeFlatEntity>> constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    
    

    
}

