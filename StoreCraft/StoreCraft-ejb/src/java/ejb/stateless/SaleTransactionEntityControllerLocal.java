/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.util.List;
import util.exception.SaleTransactionAlreadyVoidedRefundedException;
import util.exception.SaleTransactionNotFoundException;

/**
 *
 * @author shawn
 */
public interface SaleTransactionEntityControllerLocal {

    public List<SaleTransactionLineItemEntity> retrieveSaleTransactionLineItemsByProductId(Long productId);

    public List<SaleTransactionEntity> retrieveAllSaleTransactions();

    public SaleTransactionEntity retrieveSaleTransactionBySaleTransactionId(Long saleTransactionId) throws SaleTransactionNotFoundException;

    public void voidRefundSaleTransaction(Long saleTransactionId) throws SaleTransactionNotFoundException, SaleTransactionAlreadyVoidedRefundedException;

    public void deleteSaleTransaction(SaleTransactionEntity saleTransactionEntity);

    public void updateSaleTransaction(SaleTransactionEntity saleTransactionEntity);
    
}
