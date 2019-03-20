/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
        
    public SaleTransactionEntity createNewSaleTransaction(){
        //TODO
        return null;
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
        Query query = entityManager.createNamedQuery("selectAllSaleTransactionLineItemsByProductId");
        query.setParameter("inProductId", productId);
        
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
    public void deleteSaleTransaction(SaleTransactionEntity saleTransactionEntity)
    {
        throw new UnsupportedOperationException();
    }
}
