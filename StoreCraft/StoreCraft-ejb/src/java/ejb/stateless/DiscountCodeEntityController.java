/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import entity.DiscountCodeEntity;
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
import util.exception.InvalidDiscountCodeException;
import util.exception.ProductNotFoundException;

/**
 *
 * @author shawn
 */
@Stateless
@Local(DiscountCodeEntityControllerLocal.class)
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

    @Override
    public DiscountCodeEntity createNewDiscountCode(DiscountCodeEntity newDiscountCodeEntity, List<Long> customerEntityIds, List<Long> productEntityIds) throws CreateNewDiscountCodeException, InputDataValidationException {
        Set<ConstraintViolation<DiscountCodeEntity>> constraintViolations = validator.validate(newDiscountCodeEntity);

        if (constraintViolations.isEmpty()) {
            try {
                if (!customerEntityIds.isEmpty()) {
                    for (Long customerId : customerEntityIds) {
                        CustomerEntity customerEntity = customerEntityControllerLocal.retrieveCustomerByCustomerId(customerId);
                        newDiscountCodeEntity.addCustomer(customerEntity);
                    }
                }

                if (!productEntityIds.isEmpty()) {
                    for (Long productId : productEntityIds) {
                        ProductEntity productEntity = productEntityControllerLocal.retrieveProductByProductId(productId);
                        newDiscountCodeEntity.addProduct(productEntity);
                    }
                }

                em.persist(newDiscountCodeEntity);
                em.flush();

                return newDiscountCodeEntity;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null
                        && ex.getCause().getCause() != null
                        && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                    throw new CreateNewDiscountCodeException("Discount Code already exists");
                } else {
                    throw new CreateNewDiscountCodeException("An unexpected error has occurred: " + ex.getMessage());
                }
            } catch (Exception ex) {
                throw new CreateNewDiscountCodeException("An unexpected error has occurred: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public DiscountCodeEntity updateDiscountCode(DiscountCodeEntity discountCodeEntity, List<Long> customerEntityIds, List<Long> productEntityIds) throws DiscountCodeNotFoundException, InvalidDiscountCodeException, InputDataValidationException, CustomerNotFoundException, ProductNotFoundException {
        Set<ConstraintViolation<DiscountCodeEntity>> constraintViolations = validator.validate(discountCodeEntity);

        if (constraintViolations.isEmpty()) {

            if (discountCodeEntity.getDiscountCodeId() != null) {

                DiscountCodeEntity discountCodeEntityToUpdate = retrieveDiscountCodeByDiscountCodeId(discountCodeEntity.getDiscountCodeId());

                if (discountCodeEntityToUpdate.getCustomerEntities().isEmpty() && discountCodeEntityToUpdate.getProductEntities().isEmpty()) {
                    throw new InvalidDiscountCodeException("Discount Code must be associated with at least 1 customer or product");
                }

                if (customerEntityIds != null) {
                    for (CustomerEntity ce : discountCodeEntityToUpdate.getCustomerEntities()) {
                        ce.getDiscountCodeEntities().remove(discountCodeEntityToUpdate);
                    }

                    discountCodeEntityToUpdate.getCustomerEntities().clear();

                    for (Long cId : customerEntityIds) {
                        discountCodeEntityToUpdate.addCustomer(customerEntityControllerLocal.retrieveCustomerByCustomerId(cId));
                    }
                }
                
                if (productEntityIds != null) {
                    for (ProductEntity pe : discountCodeEntityToUpdate.getProductEntities()) {
                        pe.getDiscountCodeEntities().remove(discountCodeEntityToUpdate);
                    }

                    discountCodeEntityToUpdate.getProductEntities().clear();

                    for (Long pId : productEntityIds) {
                        discountCodeEntityToUpdate.addProduct(productEntityControllerLocal.retrieveProductByProductId(pId));
                    }
                }

                discountCodeEntityToUpdate.setStartDate(discountCodeEntity.getStartDate());
                discountCodeEntityToUpdate.setEndDate(discountCodeEntity.getEndDate());
                discountCodeEntityToUpdate.setNumAvailable(discountCodeEntity.getNumAvailable());
                discountCodeEntityToUpdate.setDiscountCode(discountCodeEntity.getDiscountCode());
                discountCodeEntityToUpdate.setDiscountAmountOrRate(discountCodeEntity.getDiscountAmountOrRate());
                discountCodeEntityToUpdate.setDiscountCodeTypeEnum(discountCodeEntity.getDiscountCodeTypeEnum());
                return discountCodeEntityToUpdate;

            } else {
                throw new DiscountCodeNotFoundException("Product ID not provided for product to be updated");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<DiscountCodeEntity> retrieveAllDiscountCodes() {
        Query query = em.createQuery("SELECT dc FROM DiscountCodeEntity dc ORDER BY dc.discountCodeId ASC");
        List<DiscountCodeEntity> discountCodeEntities = query.getResultList();

        for (DiscountCodeEntity discountCodeEntity : discountCodeEntities) {
            discountCodeEntity.getCustomerEntities().size();
            discountCodeEntity.getProductEntities().size();
        }

        return discountCodeEntities;
    }

    @Override
    public void updateStartDateEndDate(Long discountCodeId, Date newStartDate, Date newEndDate) throws DiscountCodeNotFoundException {
        DiscountCodeEntity discountCodeEntity = retrieveDiscountCodeByDiscountCodeId(discountCodeId);
        discountCodeEntity.setStartDate(newStartDate);
        discountCodeEntity.setEndDate(newEndDate);

    }

    @Override
    public void updateAvailable(Long discountCodeId, int numAvailable) throws DiscountCodeNotFoundException {
        DiscountCodeEntity discountCodeEntity = retrieveDiscountCodeByDiscountCodeId(discountCodeId);
        discountCodeEntity.setNumAvailable(numAvailable);
    }

    @Override
    public DiscountCodeEntity retrieveDiscountCodeByDiscountCodeId(Long discountCodeId) throws DiscountCodeNotFoundException {
        if (discountCodeId == null) {
            throw new DiscountCodeNotFoundException("Discount Code ID is not provided");
        }

        DiscountCodeEntity discountCodeEntity = em.find(DiscountCodeEntity.class, discountCodeId);

        if (discountCodeEntity == null) {
            throw new DiscountCodeNotFoundException("Discount Code " + discountCodeId + "does not exist");
        } else {
            discountCodeEntity.getCustomerEntities().size();
            discountCodeEntity.getProductEntities().size();

            return discountCodeEntity;
        }
    }

    @Override
    public void deleteDiscountCode(Long discountCodeId) throws DiscountCodeNotFoundException {
        DiscountCodeEntity discountCodeEntity = retrieveDiscountCodeByDiscountCodeId(discountCodeId);

        for (CustomerEntity c : discountCodeEntity.getCustomerEntities()) {
            c.getDiscountCodeEntities().remove(discountCodeEntity);
        }

        for (ProductEntity p : discountCodeEntity.getProductEntities()) {
            p.getDiscountCodeEntities().remove(discountCodeEntity);
        }

        discountCodeEntity.setCustomerEntities(null);
        discountCodeEntity.setProductEntities(null);

        em.remove(discountCodeEntity);

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<DiscountCodeEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
