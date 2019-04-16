/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import entity.ProductEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CustomerNotFoundException;
import util.exception.ProductInsufficientQuantityOnHandException;
import util.exception.ProductNotFoundException;
import util.exception.SaleTransactionAlreadyVoidedRefundedException;
import util.exception.SaleTransactionNotFoundException;

/**
 *
 * @author shawn
 */
@Stateless
@Local(SaleTransactionEntityControllerLocal.class)
public class SaleTransactionEntityController implements SaleTransactionEntityControllerLocal {

    @EJB(name = "CommunityGoalEntityControllerLocal")
    private CommunityGoalEntityControllerLocal communityGoalEntityControllerLocal;

    @PersistenceContext(unitName = "StoreCraft-ejbPU")
    private javax.persistence.EntityManager entityManager;
    @Resource
    private EJBContext eJBContext;
    
    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    
    
    public SaleTransactionEntityController(){
    }
        
    @Override
    public SaleTransactionEntity createNewSaleTransaction(Long customerId, SaleTransactionEntity newSaleTransactionEntity) throws CustomerNotFoundException, CreateNewSaleTransactionException{
        
        if(newSaleTransactionEntity != null)
        {
            try
            {   
                CustomerEntity customerEntity = customerEntityControllerLocal.retrieveCustomerByCustomerId(customerId);
                newSaleTransactionEntity.setCustomerEntity(customerEntity);
                customerEntity.getSaleTransactionEntities().add(newSaleTransactionEntity);

                entityManager.persist(newSaleTransactionEntity);

                for(SaleTransactionLineItemEntity saleTransactionLineItemEntity:newSaleTransactionEntity.getSaleTransactionLineItemEntities())
                {
                    productEntityControllerLocal.debitQuantityOnHand(saleTransactionLineItemEntity.getProductEntity().getProductId(), saleTransactionLineItemEntity.getQuantity());

//                    ProductEntity productEntity = productEntityControllerLocal.retrieveProductByProductId(saleTransactionLineItemEntity.getProductEntity().getProductId());
//                    saleTransactionLineItemEntity.setProductEntity(productEntity);

                    entityManager.persist(saleTransactionLineItemEntity);
                }

                entityManager.flush();
                
                customerEntityControllerLocal.updateCustomerPoints(customerEntity, newSaleTransactionEntity.getTotalAmount());
                
                BigDecimal totalPoints = newSaleTransactionEntity.getTotalAmount().multiply(new BigDecimal(20));
                communityGoalEntityControllerLocal.addPointsToCommunityGoal(customerEntity.getCountry(), totalPoints);

                return newSaleTransactionEntity;
            }
            catch(ProductNotFoundException | ProductInsufficientQuantityOnHandException ex)
            {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();
                throw new CreateNewSaleTransactionException(ex.getMessage());
            }
        }
        else
        {
            throw new CreateNewSaleTransactionException("Sale transaction information not provided");
        }
    }
    
    @Override
    public List<SaleTransactionEntity> retrieveAllSaleTransactions()
    {
        Query query = entityManager.createQuery("SELECT st FROM SaleTransactionEntity st");
        
        return query.getResultList();
    }
    
    @Override
    public List<SaleTransactionLineItemEntity> retrieveSaleTransactionLineItemsByProductId(Long productId)
    {
        Query query = entityManager.createQuery("SELECT stli FROM SaleTransactionLineItemEntity stli WHERE stli.productEntity.productId = :inProductId");
        query.setParameter("inProductId", productId);
        
        return query.getResultList();
    }
    
    @Override
    public List<SaleTransactionEntity> retrieveSaleTransactionByCustomer(Long customerId){
        Query query = entityManager.createQuery("SELECT st FROM SaleTransactionEntity st WHERE st.customerEntity.customerId = :inCustomerId");
        query.setParameter("inCustomerId", customerId);
        
        return query.getResultList();
    }
    
    @Override
    public SaleTransactionEntity retrieveSaleTransactionBySaleTransactionId(Long saleTransactionId) throws SaleTransactionNotFoundException
    {
        if(saleTransactionId == null)
        {
            throw new SaleTransactionNotFoundException("Sale Transaction ID not provided");
        }
        
        SaleTransactionEntity saleTransactionEntity = entityManager.find(SaleTransactionEntity.class, saleTransactionId);
        
        if(saleTransactionEntity != null)
        {
            saleTransactionEntity.getSaleTransactionLineItemEntities().size();
            
            return saleTransactionEntity;
        }
        else
        {
            throw new SaleTransactionNotFoundException("Sale Transaction ID " + saleTransactionId + " does not exist!");
        }                
    }
    
    @Override
    public void voidRefundSaleTransaction(Long saleTransactionId) throws SaleTransactionNotFoundException, SaleTransactionAlreadyVoidedRefundedException
    {
        SaleTransactionEntity saleTransactionEntity = retrieveSaleTransactionBySaleTransactionId(saleTransactionId);
        
        if(!saleTransactionEntity.getVoidRefund())
        {
            for(SaleTransactionLineItemEntity saleTransactionLineItemEntity:saleTransactionEntity.getSaleTransactionLineItemEntities())
            {
                try
                {
                    productEntityControllerLocal.creditQuantityOnHand(saleTransactionLineItemEntity.getProductEntity().getProductId(), saleTransactionLineItemEntity.getQuantity());
                }
                catch(ProductNotFoundException ex)
                {
                    ex.printStackTrace(); // Ignore exception since this should not happen
                }                
            }
            
            saleTransactionEntity.setVoidRefund(true);
        }
        else
        {
            throw new SaleTransactionAlreadyVoidedRefundedException("The sale transaction has aready been voided/refunded");
        }
    }
    
    
    @Override
    public void updateSaleTransaction(SaleTransactionEntity saleTransactionEntity)
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void deleteSaleTransaction(Long saleTransactionEntityId) throws SaleTransactionNotFoundException
    {   
        // Persistent context
        SaleTransactionEntity saleTransactionEntityToRemove = retrieveSaleTransactionBySaleTransactionId(saleTransactionEntityId);
        
        List<SaleTransactionLineItemEntity> saleTransactionLineItems = saleTransactionEntityToRemove.getSaleTransactionLineItemEntities();
        
        if( !saleTransactionLineItems.isEmpty() )
        {
            saleTransactionEntityToRemove.setSaleTransactionLineItemEntities(null);
            
            for(SaleTransactionLineItemEntity saleTransactionLineItem : saleTransactionLineItems)
            {   
                entityManager.remove(saleTransactionLineItem);
            }
        }
        entityManager.remove(saleTransactionEntityToRemove);
    }
}
