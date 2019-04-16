/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import entity.DiscountCodeEntity;
import entity.ProductEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import entity.StaffEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.DiscountCodeTypeEnum;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CustomerNotFoundException;
import util.exception.DiscountCodeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NegativeSaleTransactionAmountException;
import util.exception.NotEnoughPointsException;
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
    @EJB
    private DiscountCodeEntityControllerLocal discountCodeEntityControllerLocal;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public SaleTransactionEntityController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public SaleTransactionEntity createNewSaleTransaction(Long customerId, SaleTransactionEntity newSaleTransactionEntity) throws InputDataValidationException, NegativeSaleTransactionAmountException, DiscountCodeNotFoundException, NotEnoughPointsException, CustomerNotFoundException, CreateNewSaleTransactionException {

        if (newSaleTransactionEntity != null) {
            try {
                CustomerEntity customerEntity = customerEntityControllerLocal.retrieveCustomerByCustomerId(customerId);
                newSaleTransactionEntity.setCustomerEntity(customerEntity);
                customerEntity.getSaleTransactionEntities().add(newSaleTransactionEntity);

                //Discount by points
                if (newSaleTransactionEntity.getPointsToUse() != null && newSaleTransactionEntity.getPointsToUse() > 0) {
                    BigDecimal discountAmount = BigDecimal.valueOf(newSaleTransactionEntity.getPointsToUse() * 0.05); //5 cents per point
                    newSaleTransactionEntity.setTotalAmount(newSaleTransactionEntity.getTotalAmount().subtract(discountAmount));
                    //reduce customer points
                    if (customerEntity.getRemainingPoints().doubleValue() < (double) newSaleTransactionEntity.getPointsToUse()) {
                        //if remaining points not enough
                        throw new NotEnoughPointsException("You do not have enough points!");
                    }
                    customerEntity.setRemainingPoints(customerEntity.getRemainingPoints().subtract(BigDecimal.valueOf((double) newSaleTransactionEntity.getPointsToUse())));

                }

                //Discount by Discount Code
                if (newSaleTransactionEntity.getDiscountCodeEntity() != null) {
                    DiscountCodeEntity discountCodeEntity = discountCodeEntityControllerLocal.retrieveDiscountCodeByDiscountCodeId(newSaleTransactionEntity.getDiscountCodeEntity().getDiscountCodeId());
                    newSaleTransactionEntity.setDiscountCodeEntity(discountCodeEntity);
                    
                    if (discountCodeEntity.getProductEntities() == null
                            || discountCodeEntity.getProductEntities().isEmpty()) { //apply to all products
                        if (discountCodeEntity.getDiscountCodeTypeEnum().equals(DiscountCodeTypeEnum.FLAT)) {
                            BigDecimal discountAmount = discountCodeEntity.getDiscountAmountOrRate();
                            newSaleTransactionEntity.setTotalAmount(newSaleTransactionEntity.getTotalAmount().subtract(discountAmount));
                            if (newSaleTransactionEntity.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) {
                                throw new NegativeSaleTransactionAmountException("Sale Transaction Amount Cannot be Negative!");
                            }
                        } else if (discountCodeEntity.getDiscountCodeTypeEnum().equals(DiscountCodeTypeEnum.PERCENTAGE)) {
                            BigDecimal discountRate = discountCodeEntity.getDiscountAmountOrRate().divide(BigDecimal.valueOf(100.0));
                            BigDecimal discountBy = newSaleTransactionEntity.getTotalAmount().multiply(discountRate);
                            newSaleTransactionEntity.setTotalAmount(newSaleTransactionEntity.getTotalAmount().subtract(discountBy));
                        }
                        
                    } else { //apply only to some products
                        List<SaleTransactionLineItemEntity> lineItemsToDiscount = this.getLineItemsToApplyDiscountTo(discountCodeEntity, newSaleTransactionEntity.getSaleTransactionLineItemEntities());
                        
                        if (discountCodeEntity.getDiscountCodeTypeEnum().equals(DiscountCodeTypeEnum.FLAT)) {
                            
                            BigDecimal discountAmount = discountCodeEntity.getDiscountAmountOrRate();
                            
                            for (SaleTransactionLineItemEntity lineItem : lineItemsToDiscount) {
                                lineItem.setSubTotal(lineItem.getSubTotal().subtract(discountAmount));
                                if (lineItem.getSubTotal().compareTo(BigDecimal.ZERO) < 0) {
                                    throw new NegativeSaleTransactionAmountException("Sale Transaction Amount Cannot be Negative!");
                                }
                            }
                            
                        } else if (discountCodeEntity.getDiscountCodeTypeEnum().equals(DiscountCodeTypeEnum.PERCENTAGE)) {
                            
                            BigDecimal discountRate = discountCodeEntity.getDiscountAmountOrRate();
                            
                            for (SaleTransactionLineItemEntity lineItem : lineItemsToDiscount) {
                                BigDecimal discountBy = lineItem.getSubTotal().multiply(discountRate);
                                lineItem.setSubTotal(lineItem.getSubTotal().subtract(discountBy));
                            }
                        }
                    }
                    discountCodeEntity.setNumAvailable(discountCodeEntity.getNumAvailable()-1);
                    discountCodeEntity.getSaleTransactionEntities().add(newSaleTransactionEntity);
                }
                
               // System.out.println(newSaleTransactionEntity.getTotalAmount());
                Set<ConstraintViolation<SaleTransactionEntity>> constraintViolations = validator.validate(newSaleTransactionEntity);
                //System.out.println(constraintViolations);

                if (constraintViolations.isEmpty()){
                    entityManager.persist(newSaleTransactionEntity);
                } else {
                    throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
                }

                for (SaleTransactionLineItemEntity saleTransactionLineItemEntity : newSaleTransactionEntity.getSaleTransactionLineItemEntities()) {
                    productEntityControllerLocal.debitQuantityOnHand(saleTransactionLineItemEntity.getProductEntity().getProductId(), saleTransactionLineItemEntity.getQuantity());

//                    ProductEntity productEntity = productEntityControllerLocal.retrieveProductByProductId(saleTransactionLineItemEntity.getProductEntity().getProductId());
//                    saleTransactionLineItemEntity.setProductEntity(productEntity);
                    entityManager.persist(saleTransactionLineItemEntity);
                }

                entityManager.flush();

                customerEntityControllerLocal.updateCustomerPoints(customerEntity, newSaleTransactionEntity.getTotalAmount());
                
                BigDecimal totalPoints = newSaleTransactionEntity.getTotalAmount().multiply(customerEntity.getMultiplier());
                communityGoalEntityControllerLocal.addPointsToCommunityGoal(customerEntity.getCountry(), totalPoints);

                return newSaleTransactionEntity;
            } catch (ProductNotFoundException | ProductInsufficientQuantityOnHandException | NotEnoughPointsException | NegativeSaleTransactionAmountException ex) {
                // The line below rolls back all changes made to the database.
                eJBContext.setRollbackOnly();
                throw new CreateNewSaleTransactionException(ex.getMessage());
            }
        } else {
            throw new CreateNewSaleTransactionException("Sale transaction information not provided");

        }
    }

    public List<SaleTransactionLineItemEntity> getLineItemsToApplyDiscountTo(DiscountCodeEntity discountCodeEntity, List<SaleTransactionLineItemEntity> lineItems) {
        List<SaleTransactionLineItemEntity> saleTransactionsToDiscount = new ArrayList<>();
        List<ProductEntity> discountCodeProducts = discountCodeEntity.getProductEntities();
        for (ProductEntity discountCodeProduct : discountCodeProducts) {
            for (SaleTransactionLineItemEntity lineItem : lineItems) {
                if (discountCodeProduct.equals(lineItem.getProductEntity())) {
                    saleTransactionsToDiscount.add(lineItem);
                }
            }
        }

        return saleTransactionsToDiscount;

    }

    @Override
    public List<SaleTransactionEntity> retrieveAllSaleTransactions() {
        Query query = entityManager.createQuery("SELECT st FROM SaleTransactionEntity st");

        return query.getResultList();
    }

    @Override
    public List<SaleTransactionLineItemEntity> retrieveSaleTransactionLineItemsByProductId(Long productId) {
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
    public SaleTransactionEntity retrieveSaleTransactionBySaleTransactionId(Long saleTransactionId) throws SaleTransactionNotFoundException {
        if (saleTransactionId == null) {
            throw new SaleTransactionNotFoundException("Sale Transaction ID not provided");
        }

        SaleTransactionEntity saleTransactionEntity = entityManager.find(SaleTransactionEntity.class, saleTransactionId);

        if (saleTransactionEntity != null) {
            saleTransactionEntity.getSaleTransactionLineItemEntities().size();

            return saleTransactionEntity;
        } else {
            throw new SaleTransactionNotFoundException("Sale Transaction ID " + saleTransactionId + " does not exist!");
        }
    }

    @Override
    public void voidRefundSaleTransaction(Long saleTransactionId) throws SaleTransactionNotFoundException, SaleTransactionAlreadyVoidedRefundedException {
        SaleTransactionEntity saleTransactionEntity = retrieveSaleTransactionBySaleTransactionId(saleTransactionId);

        if (!saleTransactionEntity.getVoidRefund()) {
            for (SaleTransactionLineItemEntity saleTransactionLineItemEntity : saleTransactionEntity.getSaleTransactionLineItemEntities()) {
                try {
                    productEntityControllerLocal.creditQuantityOnHand(saleTransactionLineItemEntity.getProductEntity().getProductId(), saleTransactionLineItemEntity.getQuantity());
                } catch (ProductNotFoundException ex) {
                    ex.printStackTrace(); // Ignore exception since this should not happen
                }
            }

            saleTransactionEntity.setVoidRefund(true);
        } else {
            throw new SaleTransactionAlreadyVoidedRefundedException("The sale transaction has aready been voided/refunded");
        }
    }

    @Override
    public void updateSaleTransaction(SaleTransactionEntity saleTransactionEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteSaleTransaction(Long saleTransactionEntityId) throws SaleTransactionNotFoundException {
        // Persistent context
        SaleTransactionEntity saleTransactionEntityToRemove = retrieveSaleTransactionBySaleTransactionId(saleTransactionEntityId);

        List<SaleTransactionLineItemEntity> saleTransactionLineItems = saleTransactionEntityToRemove.getSaleTransactionLineItemEntities();

        if (!saleTransactionLineItems.isEmpty()) {
            saleTransactionEntityToRemove.setSaleTransactionLineItemEntities(null);

            for (SaleTransactionLineItemEntity saleTransactionLineItem : saleTransactionLineItems) {
                entityManager.remove(saleTransactionLineItem);
            }
        }
        entityManager.remove(saleTransactionEntityToRemove);
    }
    
     private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<SaleTransactionEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
