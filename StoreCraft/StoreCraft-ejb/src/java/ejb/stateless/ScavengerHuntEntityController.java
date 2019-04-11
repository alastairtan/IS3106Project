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
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RewardTypeEnum;
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
    
    @Override
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
    
    public List<ScavengerHuntEntity> retrieveAllScavengerHunts()
    {
        Query query = entityManager.createQuery("SELECT p FROM ScavengerHuntEntity p ORDER BY p.scavengerHuntDate ASC");
        List<ScavengerHuntEntity> scavengers = query.getResultList();
        
        for(ScavengerHuntEntity s:scavengers)
        {
            s.getProductEntity();
            s.getCustomerEntities().size();
        }
        
        return scavengers;
    }
    
    // EJB Timer - create a scavengerHuntEntity every day
    @Schedule(dayOfWeek="*")
    @Override
    public void createScavengerHuntEntity(){
        List<ProductEntity> products = productEntityControllerLocal.retrieveAllProducts();
        int random = (int)Math.random() * products.size();
        ScavengerHuntEntity scHunt = new ScavengerHuntEntity(new Date(), 5);
        //set product
        
                scHunt.setProductEntity(products.get(random));
        
        
        scHunt.setNumWinnersRemaining(5);
        entityManager.persist(scHunt);
        entityManager.flush();
        
    }
    
    // Every time people click on the product (productEntityController), update the scavengerHuntEntity 
    
    /*
        Triggered by productEntityController method
    */
    @Override
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
    
    @Override
    public boolean hasCustomerWonToday(Long customerId) throws ScavengerHuntNotFoundException{
        Date today = new Date();
        ScavengerHuntEntity todaysHunt = retrieveScavengerHuntEntityByDate(today);
        for (CustomerEntity ce : todaysHunt.getCustomerEntities()){
            if (ce.getCustomerId().equals(customerId)){
                return true;
            }
        }
        return false;
    }
    
    
}
