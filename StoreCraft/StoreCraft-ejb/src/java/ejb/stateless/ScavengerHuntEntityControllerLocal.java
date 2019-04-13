/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import entity.ScavengerHuntEntity;
import java.util.Date;
import java.util.List;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ScavengerHuntAlreadyExistException;
import util.exception.ScavengerHuntNotFoundException;

/**
 *
 * @author shawn
 */

public interface ScavengerHuntEntityControllerLocal {

    public ScavengerHuntEntity retrieveScavengerHuntEntityByDate(Date date) throws ScavengerHuntNotFoundException;

    public void updateWinnerForScavengerHunt(CustomerEntity customerEntity) throws ScavengerHuntNotFoundException, CustomerNotFoundException;

    public boolean hasCustomerWonToday(Long customerId) throws ScavengerHuntNotFoundException;

    public void createScavengerHuntEntity() throws ScavengerHuntAlreadyExistException;

    public List<ScavengerHuntEntity> retrieveAllScavengerHunts();

    public ScavengerHuntEntity retrieveScavengerHuntEntityById(Long id) throws ScavengerHuntNotFoundException;

    public ScavengerHuntEntity removeScavengerHuntEntity(Long id) throws ScavengerHuntNotFoundException;

    public ScavengerHuntEntity updateScavengerHuntEntity(ScavengerHuntEntity scHunt) throws ScavengerHuntNotFoundException, InputDataValidationException;
    
}
