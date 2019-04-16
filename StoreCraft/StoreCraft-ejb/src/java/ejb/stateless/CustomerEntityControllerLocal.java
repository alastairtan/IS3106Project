/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import java.math.BigDecimal;
import java.util.List;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author shawn
 */

public interface CustomerEntityControllerLocal {

    public List<CustomerEntity> retrieveAllCustomer();

    public CustomerEntity retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginCredentialException;

    public void updateCustomerDetails(CustomerEntity customerEntity) throws CustomerNotFoundException, InputDataValidationException, UpdateCustomerException;

    public void updatePassword(CustomerEntity customerEntity, String oldPasword, String newPassword) throws CustomerNotFoundException, InvalidLoginCredentialException;

    public void updateCustomerPoints(CustomerEntity customerEntity, BigDecimal basePointToUpdate) throws CustomerNotFoundException;

    public CustomerEntity createNewCustomer(CustomerEntity newCustomerEntity) throws InputDataValidationException, Exception;

    public List<CustomerEntity> retrieveCustomersBySpendingPerMonth();

    public List<CustomerEntity> retrieveCustomersBySpendingTotal();


}
