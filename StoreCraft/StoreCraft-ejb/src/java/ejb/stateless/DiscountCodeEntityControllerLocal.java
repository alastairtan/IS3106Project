/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.DiscountCodeEntity;
import java.util.Date;
import java.util.List;
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
public interface DiscountCodeEntityControllerLocal {

    public List<DiscountCodeEntity> retrieveAllDiscountCodes();

    public void updateStartDateEndDate(Long discountCodeId, Date newStartDate, Date newEndDate) throws DiscountCodeNotFoundException;

    public void updateAvailable(Long discountCodeId, int numAvailable) throws DiscountCodeNotFoundException;

    public DiscountCodeEntity retrieveDiscountCodeByDiscountCodeId(Long discountCodeId) throws DiscountCodeNotFoundException;

    public void deleteDiscountCode(Long discountCodeId) throws DiscountCodeNotFoundException;

    public DiscountCodeEntity createNewDiscountCode(DiscountCodeEntity newDiscountCodeEntity, List<Long> customerEntityIds, List<Long> productEntityIds) throws CreateNewDiscountCodeException, InputDataValidationException;

    public DiscountCodeEntity updateDiscountCode(DiscountCodeEntity discountCodeEntity, List<Long> customerEntityIds, List<Long> productEntityIds) throws DiscountCodeNotFoundException, InvalidDiscountCodeException, InputDataValidationException, CustomerNotFoundException, ProductNotFoundException;
    
}
