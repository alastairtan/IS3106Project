/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.ProductEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.math.BigDecimal;
import java.util.List;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CustomerNotFoundException;
import util.exception.DiscountCodeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NegativeSaleTransactionAmountException;
import util.exception.NotEnoughPointsException;
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

    public void updateSaleTransaction(SaleTransactionEntity saleTransactionEntity);

    public List<SaleTransactionEntity> retrieveSaleTransactionByCustomer(Long customerId);

    public void deleteSaleTransaction(Long saleTransactionEntityId) throws SaleTransactionNotFoundException;

    public SaleTransactionEntity createNewSaleTransaction(Long customerId, SaleTransactionEntity newSaleTransactionEntity) throws InputDataValidationException, NegativeSaleTransactionAmountException, DiscountCodeNotFoundException, NotEnoughPointsException, CustomerNotFoundException, CreateNewSaleTransactionException;

    public List<BigDecimal> retrieveSaleTransactionForTheYear();

    public List<ProductEntity> retrieveTopSellingProductsAllTime();

    public int numberOfProductsSold(Long productId);
    
}
