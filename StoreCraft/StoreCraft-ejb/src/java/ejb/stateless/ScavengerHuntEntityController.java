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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ScavengerHuntAlreadyExistException;
import util.exception.ScavengerHuntNotFoundException;

/**
 *
 * @author shawn
 */
@Stateless
@Local(ScavengerHuntEntityControllerLocal.class)

public class ScavengerHuntEntityController implements ScavengerHuntEntityControllerLocal {

    @PersistenceContext(unitName = "StoreCraft-ejbPU")
    private EntityManager entityManager;

    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ScavengerHuntEntityController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    // keep track of all the winner
    @Override
    public ScavengerHuntEntity retrieveScavengerHuntEntityByDate(Date date) throws ScavengerHuntNotFoundException {
        GregorianCalendar calendarIn = new GregorianCalendar();
        calendarIn.setTime(date);

        List<ScavengerHuntEntity> allScavengerHunts = retrieveAllScavengerHunts();

        for (ScavengerHuntEntity s : allScavengerHunts) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(s.getScavengerHuntDate());
            if (calendarIn.get(GregorianCalendar.YEAR) == calendar.get(GregorianCalendar.YEAR)
                    && calendarIn.get(GregorianCalendar.DAY_OF_YEAR) == calendar.get(GregorianCalendar.DAY_OF_YEAR)) {
                s.getCustomerEntities().size();
                return s;
            }
        }

        throw new ScavengerHuntNotFoundException("Scavenger hunt for date: " + date + " does not exist!");

    }

    @Override
    public List<ScavengerHuntEntity> retrieveAllScavengerHunts() {
        Query query = entityManager.createQuery("SELECT p FROM ScavengerHuntEntity p ORDER BY p.scavengerHuntDate ASC");
        List<ScavengerHuntEntity> scavengers = query.getResultList();

        for (ScavengerHuntEntity s : scavengers) {
            s.getCustomerEntities().size();
        }

        return scavengers;
    }

    // EJB Timer - create a scavengerHuntEntity every day
    @Schedule(dayOfWeek = "*")
    @Override
    public void createScavengerHuntEntity() throws ScavengerHuntAlreadyExistException {
        List<ProductEntity> products = productEntityControllerLocal.retrieveAllProducts();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
        calendar.set(GregorianCalendar.MINUTE, 0);
        Date date = calendar.getTime();
        try {
            if (retrieveScavengerHuntEntityByDate(date) != null) {
                throw new ScavengerHuntAlreadyExistException("Only one scavenger hunt allowed per day");
            }
        } catch (ScavengerHuntNotFoundException ex) {

            ScavengerHuntEntity scHunt = new ScavengerHuntEntity(date, 5);

            List<ProductEntity> allProducts = productEntityControllerLocal.retrieveAllProducts();
            for (ProductEntity p : allProducts) {
                p.setIsScavengerHuntPrize(false);
            }

            int numSet = 5;
            while (numSet > 0) {
                int random = (int) (Math.random() * products.size());

                if (products.get(random).getIsScavengerHuntPrize()) {
                } else {
                    products.get(random).setIsScavengerHuntPrize(true);
                    numSet--;
                }
            }

            System.out.println("ejb.stateless.ScavengerHuntEntityController.createScavengerHuntEntity()");

            entityManager.persist(scHunt);
            entityManager.flush();
        }
    }

    // Every time people click on the product (productEntityController), update the scavengerHuntEntity 
    /*
        Triggered by productEntityController method
     */
    @Override
    public void updateWinnerForScavengerHunt(CustomerEntity customerEntity) throws ScavengerHuntNotFoundException, CustomerNotFoundException {
        if (customerEntity.getCustomerId() != null) {
            // retrieve by today's date
            ScavengerHuntEntity scavengerHuntEntity = retrieveScavengerHuntEntityByDate(new Date());

            scavengerHuntEntity.getCustomerEntities().add(customerEntity);
            scavengerHuntEntity.setNumWinnersRemaining(scavengerHuntEntity.getNumWinnersRemaining() - 1);

            /*
                OR return a boolean value to productEntityController method
                OR have a unidirectional link to producttEntityController
             */
            if (scavengerHuntEntity.getNumWinnersRemaining() == 0) {
                List<ProductEntity> productEntities = productEntityControllerLocal.retrieveAllProducts();

                for (ProductEntity productEntity : productEntities) {
                    productEntity.setIsScavengerHuntPrize(Boolean.FALSE);
                }
            }
        } else {
            throw new CustomerNotFoundException("Customer with ID: " + customerEntity.getId() + " doesn't exist!");
        }
    }

    @Override
    public boolean hasCustomerWonToday(Long customerId) throws ScavengerHuntNotFoundException {
        Date today = new Date();
        ScavengerHuntEntity todaysHunt = retrieveScavengerHuntEntityByDate(today);
        for (CustomerEntity ce : todaysHunt.getCustomerEntities()) {
            if (ce.getCustomerId().equals(customerId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ScavengerHuntEntity retrieveScavengerHuntEntityById(Long id) throws ScavengerHuntNotFoundException {
        Query query = entityManager.createQuery("SELECT sh FROM ScavengerHuntEntity sh WHERE sh.scavengerHuntId = :inId");
        query.setParameter("inId", id);

        try {
           ScavengerHuntEntity sh = (ScavengerHuntEntity) query.getSingleResult();
           sh.getCustomerEntities().size();
           return sh;
        } catch (NoResultException ex) {
            throw new ScavengerHuntNotFoundException("Scavenger Hunt Not Found");
        }
    }

    @Override
    public ScavengerHuntEntity removeScavengerHuntEntity(Long id) throws ScavengerHuntNotFoundException {
        ScavengerHuntEntity s = retrieveScavengerHuntEntityById(id);
        GregorianCalendar calendarNow = new GregorianCalendar();
        calendarNow.setTime(new Date());

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(s.getScavengerHuntDate());
        if (calendarNow.get(GregorianCalendar.YEAR) == calendar.get(GregorianCalendar.YEAR)
                && calendarNow.get(GregorianCalendar.DAY_OF_YEAR) == calendar.get(GregorianCalendar.DAY_OF_YEAR)) {
            List<ProductEntity> allProducts = productEntityControllerLocal.retrieveAllProducts();
            for (ProductEntity p : allProducts) {
                p.setIsScavengerHuntPrize(false);
            }
        }

        s.setCustomerEntities(null);
        entityManager.remove(s);
        entityManager.flush();
        return s;
    }
    
    @Override
    public ScavengerHuntEntity updateScavengerHuntEntity(ScavengerHuntEntity scHunt) throws ScavengerHuntNotFoundException, InputDataValidationException{
        ScavengerHuntEntity s = retrieveScavengerHuntEntityById(scHunt.getScavengerHuntId());
        s.setRewardTypeEnum(scHunt.getRewardTypeEnum());
        s.setNumWinnersRemaining(scHunt.getNumWinnersRemaining());
        
         Set<ConstraintViolation<ScavengerHuntEntity>>constraintViolations = validator.validate(s);
        
        if(constraintViolations.isEmpty()){
            return s;
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ScavengerHuntEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
