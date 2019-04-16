/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import entity.DiscountCodeEntity;
import entity.ProductEntity;
import entity.ScavengerHuntEntity;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
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
import util.enumeration.DiscountCodeTypeEnum;
import util.enumeration.RewardTypeEnum;
import util.exception.CreateNewDiscountCodeException;
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
    private DiscountCodeEntityControllerLocal discountCodeEntityControllerLocal;

    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    
    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;
    
    

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
    public ScavengerHuntEntity updateWinnerForScavengerHunt(Long customerId) throws ScavengerHuntNotFoundException, CustomerNotFoundException,
                                                            CreateNewDiscountCodeException, InputDataValidationException {
        if (customerId != null) {
            // retrieve by today's date
            ScavengerHuntEntity scavengerHuntEntity = retrieveScavengerHuntEntityByDate(new Date());
            
            CustomerEntity customerEntityFromDb = customerEntityControllerLocal.retrieveCustomerByCustomerId(customerId);
          
            // Giving the reward to winner
            if (scavengerHuntEntity.getRewardTypeEnum() != RewardTypeEnum.POINTS) {
                
                Random rand = new Random();
                String characters = "ABCDEFGHIJKLMNOPQRSTWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                String discountCode = "";
                int discountCodeLength = 6;
                BigDecimal discountAmount;
                char[] text = new char[discountCodeLength];
                
                // Generate discount code of length 6
                for(int i = 0; i < discountCodeLength; i++)
                {
                    text[i] = characters.charAt(rand.nextInt(characters.length()));
                    discountCode += text[i];
                }
                
                // ---------- Code to convert date to localTime for adding --------------- //
                
                final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
                final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                final DateTimeFormatter dateFormat8 = DateTimeFormatter.ofPattern(DATE_FORMAT);

                Date currentDate = new Date();
                System.out.println("date : " + dateFormat.format(currentDate));

                LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                System.out.println("localDateTime : " + dateFormat8.format(localDateTime));
                
                // ------------------------------------------------------------------------ //
                
                localDateTime = localDateTime.plusDays(20); // plus 20 days
                // convert LocalDateTime back to date
                Date endDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                
                // Declare all variables
                DiscountCodeEntity discountCodeEntity;
                List<Long> customerIds = new ArrayList<>();
                List<Long> productIds = new ArrayList<>();
                
                customerIds.add(customerId);                
                    
                switch(scavengerHuntEntity.getRewardTypeEnum()) 
                {
                    case DISCOUNT_CODE_FLAT:
                        
                        // 10 - 20 dollars flat rate discount
                        do {
                            discountAmount = new BigDecimal(rand.nextInt(20));
                        } while (discountAmount.compareTo(new BigDecimal(10)) != -1);
                        
                        discountCodeEntity = new DiscountCodeEntity(currentDate, endDate, 1, discountCode, DiscountCodeTypeEnum.FLAT, discountAmount);
                        discountCodeEntityControllerLocal.createNewDiscountCode(discountCodeEntity, customerIds, productIds);
                        

                        break;
                        
                    case DISCOUNT_CODE_PERCENTAGE:
                        
                        // 5 - 10% percentage discount
                        do {
                            discountAmount = new BigDecimal(rand.nextInt(10));
                        } while (discountAmount.compareTo(new BigDecimal(5)) != -1);

                        discountCodeEntity = new DiscountCodeEntity(currentDate, endDate, 1, discountCode, DiscountCodeTypeEnum.PERCENTAGE, discountAmount);
                        discountCodeEntityControllerLocal.createNewDiscountCode(discountCodeEntity, customerIds, productIds);
                        
                        break;   
                }
            } 
            else
            {
                BigDecimal rewardPoint;

                do {
                    rewardPoint = new BigDecimal(Math.random() * 100);
                } while (rewardPoint.compareTo(new BigDecimal(50)) != -1 && rewardPoint.compareTo(new BigDecimal(20)) != 1);
                    

                customerEntityFromDb.setPointsForCurrentMonth(customerEntityFromDb.getPointsForCurrentMonth().add(rewardPoint));
                customerEntityFromDb.setRemainingPoints(customerEntityFromDb.getRemainingPoints().add(rewardPoint));
                customerEntityFromDb.setTotalPoints(customerEntityFromDb.getTotalPoints().add(rewardPoint));
            }
            
            // Once reward has been properly given out, update the scavenger hunt entity
            scavengerHuntEntity.getCustomerEntities().add(customerEntityFromDb);
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
            
            return scavengerHuntEntity;
            
        } else {
            throw new CustomerNotFoundException("Customer with ID: " + customerId + " doesn't exist!");
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
