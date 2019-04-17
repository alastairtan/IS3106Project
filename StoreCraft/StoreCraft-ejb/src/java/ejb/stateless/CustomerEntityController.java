/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.MembershipTierEnum;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UpdateCustomerException;
import util.security.CryptographicHelper;


@Stateless
@Local (CustomerEntityControllerLocal.class)

public class CustomerEntityController implements CustomerEntityControllerLocal {

    @PersistenceContext(unitName = "StoreCraft-ejbPU")
    private EntityManager entityManager;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public CustomerEntityController() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public CustomerEntity createNewCustomer(CustomerEntity newCustomerEntity) throws InputDataValidationException, Exception
    {
        Set<ConstraintViolation<CustomerEntity>> constraintViolations = validator.validate(newCustomerEntity);
        
        if(constraintViolations.isEmpty())
        {
            try {
                entityManager.persist(newCustomerEntity);
                entityManager.flush();
            
                return newCustomerEntity;
            } 
            catch (Exception ex)
            {
                throw new Exception(ex.getMessage());
            }
            
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<CustomerEntity> retrieveAllCustomer()
    {
        Query query = entityManager.createQuery("SELECT c FROM CustomerEntity c");
        
        return query.getResultList();
    }
    
    @Override
    public CustomerEntity retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException
    {
        if(customerId == null)
        {
            throw new CustomerNotFoundException("Customer ID not provided");
        }
        
        CustomerEntity customerEntity = entityManager.find(CustomerEntity.class, customerId);
        
        if(customerEntity != null)
        {
            return customerEntity;
        }
        else
        {
            throw new CustomerNotFoundException("Customer ID " + customerId + " does not exist!");
        }
    }
    
    @Override
    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException
    {
        Query query = entityManager.createQuery("SELECT c FROM CustomerEntity c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);
        
        try
        {
            return (CustomerEntity) query.getSingleResult();
        }
        catch (NoResultException | NonUniqueResultException ex)
        {
            throw new CustomerNotFoundException("Customer email " + email + "does not exist!");
        }
    }
    
    @Override
    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginCredentialException
    {
        try
        {
            CustomerEntity customerEntity = retrieveCustomerByEmail(email);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + customerEntity.getSalt()));
            
            if(customerEntity.getPassword().equals(passwordHash))
            {
                updateMembership(customerEntity);
                return customerEntity;
            }
            else
            {
                throw new InvalidLoginCredentialException("Email does not exist or invalid password!");
            }
        }
        catch (CustomerNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }
    
    // Does not include password
    @Override
    public void updateCustomerDetails(CustomerEntity customerEntity) throws CustomerNotFoundException, InputDataValidationException, UpdateCustomerException
    {
        Set<ConstraintViolation<CustomerEntity>> constraintViolations = validator.validate(customerEntity);
        
        if(constraintViolations.isEmpty() )
        {
            // Check if the customerEntity is valid (not covered by validator)
            if(customerEntity.getCustomerId() != null)
            {   
                // customerEntityToUpdate is in the persistence context
                CustomerEntity customerEntityToUpdate = retrieveCustomerByCustomerId(customerEntity.getCustomerId());
                updateMembership(customerEntityToUpdate);
                
                // Ensure that the email(username) is not changed
                if(customerEntityToUpdate.getEmail().equals(customerEntity.getEmail()))
                {
                    customerEntityToUpdate.setFirstName(customerEntity.getFirstName());
                    customerEntityToUpdate.setLastName(customerEntity.getLastName());
                    customerEntityToUpdate.setCountry(customerEntity.getCountry());
                    customerEntityToUpdate.setProfilePicUrl(customerEntityToUpdate.getProfilePicUrl());
                }
                else
                {
                    throw new UpdateCustomerException("Email of staff record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new CustomerNotFoundException("Customer ID not provided for customer to be updated");
            }
        }
        else
        {
        throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public void updatePassword(CustomerEntity customerEntity, String oldPasword, String newPassword) throws CustomerNotFoundException, InvalidLoginCredentialException
    {   
        if(customerEntity.getCustomerId() != null)
        {   
            // Persistent context
            CustomerEntity customerEntityToUpdate = retrieveCustomerByCustomerId(customerEntity.getCustomerId());
            
            String oldPasswordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(oldPasword + customerEntity.getSalt()));
        
            if(customerEntity.getPassword().equals(oldPasswordHash))
            {   
                customerEntityToUpdate.setSalt(CryptographicHelper.getInstance().generateRandomString(32));
                customerEntityToUpdate.setPassword(newPassword);
            }
            else
            {
                throw new InvalidLoginCredentialException("The old password is incorrect!");
            }
        }
        else
        {
            throw new CustomerNotFoundException("Customer ID not provided for customer to update password");
        }
    }
    
    /*
    Triggering event:
    - Customer check out +/ made payment
    - Purchase discount (minus remaining points) 
    - Monthly reset of pointOfTheMonth (EJB Timer Service)
    
        // Update remaining and total point (check multiplier/membership)
        // Update pointOfTheMonth -- Timer to reset every month
        // Update multiplier/membership accordingly based on total point
    
    */
    @Override
    public void updateCustomerPoints(CustomerEntity customerEntity, BigDecimal basePointToUpdate) throws CustomerNotFoundException
    {
        if(customerEntity.getCustomerId() != null)
        {
            // **** Persistent context **** //
            CustomerEntity customerEntityToUpdate = retrieveCustomerByCustomerId(customerEntity.getCustomerId());
            // If point to update is positive non-zero
            if(basePointToUpdate.compareTo(BigDecimal.ZERO) == 1)
            {
                BigDecimal multiplier = customerEntity.getMultiplier();
                BigDecimal actualPointToUpdate = basePointToUpdate.multiply(multiplier);
            
                customerEntityToUpdate.setPointsForCurrentMonth(customerEntity.getPointsForCurrentMonth().add(actualPointToUpdate));
                customerEntityToUpdate.setRemainingPoints(customerEntity.getRemainingPoints().add(actualPointToUpdate));
                customerEntityToUpdate.setTotalPoints(customerEntity.getTotalPoints().add(actualPointToUpdate));
                
                // Update multiplier / membership tier if necessary
                updateMembership(customerEntityToUpdate);
            }
            // If point to update is negative non-zero
            else if (basePointToUpdate.compareTo(BigDecimal.ZERO) == -1)
            {
                customerEntityToUpdate.setRemainingPoints(customerEntity.getRemainingPoints().add(basePointToUpdate));
            }
        }
    }
    
    private void updateMembership(CustomerEntity c)
    {
        CustomerEntity customerEntity = entityManager.find(CustomerEntity.class, c.getCustomerId());
        if (customerEntity.getTotalPoints().compareTo(new BigDecimal("600")) >= 1)
        {
            customerEntity.setMembershipTierEnum(MembershipTierEnum.GRANDMASTER);
            customerEntity.setMultiplier(new BigDecimal("2.5"));
        }
        else if (customerEntity.getTotalPoints().compareTo(new BigDecimal("500")) >= 1)
        {
            customerEntity.setMembershipTierEnum(MembershipTierEnum.DIAMOND);
            customerEntity.setMultiplier(new BigDecimal("2.0"));
            System.out.print("mem 1");
        }
        else if (customerEntity.getTotalPoints().compareTo(new BigDecimal("400")) >= 1)
        {
            customerEntity.setMembershipTierEnum(MembershipTierEnum.PLATINUM);
            customerEntity.setMultiplier(new BigDecimal("1.6"));
             System.out.print("mem 2");
        }
        else if (customerEntity.getTotalPoints().compareTo(new BigDecimal("300")) >= 1)
        {
            customerEntity.setMembershipTierEnum(MembershipTierEnum.GOLD);
            customerEntity.setMultiplier(new BigDecimal("1.4"));
        }
        else if (customerEntity.getTotalPoints().compareTo(new BigDecimal("200")) >= 1)
        {
            customerEntity.setMembershipTierEnum(MembershipTierEnum.SILVER);
            customerEntity.setMultiplier(new BigDecimal("1.2"));
        }
    }
    
    
    @Override
    public List<CustomerEntity> retrieveCustomersBySpendingPerMonth() {
        Query query = entityManager.createQuery("SELECT ce FROM CustomerEntity ce ORDER BY ce.pointsForCurrentMonth DESC");
        List<CustomerEntity> customerEntitys = query.getResultList();
        
        System.out.println("customerEntitys by spending month: " + customerEntitys.size());
        
        return customerEntitys;
    }
    
    @Override
    public List<CustomerEntity> retrieveCustomersBySpendingTotal() {
        Query query = entityManager.createQuery("SELECT ce FROM CustomerEntity ce ORDER BY ce.totalPoints DESC");
        List<CustomerEntity> customerEntitys = query.getResultList();
        System.out.println("customerEntitys by TOTAL SPENDING: " + customerEntitys.size());
        
        return customerEntitys;
        
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CustomerEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
    
    
}
