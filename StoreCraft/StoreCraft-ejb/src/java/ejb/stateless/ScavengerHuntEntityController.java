/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import entity.ProductEntity;
import entity.ScavengerHuntEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.ScavengerHuntNotFoundException;

/**
 *
 * @author shawn
 */
@Stateless
@Local (ScavengerHuntEntityControllerLocal.class)

public class ScavengerHuntEntityController implements ScavengerHuntEntityControllerLocal {
    
    @PersistenceContext(unitName = "StoreCraft-ejbPU")
    private EntityManager entityManager;
    
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;

    
    
    public ScavengerHuntEntityController() {
    }
    
    // keep track of all the winner
    
    public ScavengerHuntEntity retrieveScavengerHuntEntityByDate(Date date) throws ScavengerHuntNotFoundException
    {
        Query query = entityManager.createQuery("SELECT sh FROM ScavengerHuntEntity sh WHERE sh.scavengerHuntDate = :inDate");
        query.setParameter("inDate", date);
        
        try
        {
            return (ScavengerHuntEntity) query.getSingleResult();
        }
        catch (NoResultException | NonUniqueResultException ex)
        {
            throw new ScavengerHuntNotFoundException("Scavenger hunt for date: " + date + " does not exist!");
        }
    }
    
    // EJB Timer - create a scavengerHuntEntity every day
    // Every time people click on the product (productEntityController), update the scavengerHuntEntity 
    
    /*
        Triggered by productEntityController method
    */
    public void updateWinnerForScavengerHunt(CustomerEntity customerEntity) throws ScavengerHuntNotFoundException, CustomerNotFoundException
    {
        if(customerEntity.getCustomerId() != null)
        {   
            // retrieve by today's date
            ScavengerHuntEntity scavengerHuntEntity = retrieveScavengerHuntEntityByDate(new Date());
            
            scavengerHuntEntity.getCustomerEntities().add(customerEntity);
            scavengerHuntEntity.setNumWinnersRemaining(scavengerHuntEntity.getNumWinnersRemaining() - 1);
            
            /*
                OR return a boolean value to productEntityController method
                OR have a unidirectional link to producttEntityController
            */
            if(scavengerHuntEntity.getNumWinnersRemaining() == 0)
            {
                List<ProductEntity> productEntities = productEntityControllerLocal.retrieveAllProducts();
                
                for(ProductEntity productEntity : productEntities)
                {
                    productEntity.setIsScavengerHuntPrize(Boolean.FALSE);
                }
            }
        }
        else
        {
            throw new CustomerNotFoundException("Customer with ID: " + customerEntity.getId() + " doesn't exist!");
        }
    }
    
    
    
}
