/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import entity.DiscountCodeEntity;
import entity.ProductEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.DiscountCodeTypeEnum;
import util.exception.CreateNewDiscountCodeException;
import util.exception.InputDataValidationException;
import util.security.CryptographicHelper;

@Stateless
@Local(EjbTimerSessionBeanLocal.class)

public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal {

    @PersistenceContext(unitName = "StoreCraft-ejbPU")
    private EntityManager entityManager;

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;

    @EJB
    private DiscountCodeEntityControllerLocal discountCodeEntityControllerLocal;

    @Schedule(month = "*", dayOfMonth = "1", info = "resetPointOfTheMonth")
    public void resetPointOfTheMonth() {
        List<CustomerEntity> customerEntities = customerEntityControllerLocal.retrieveAllCustomer();

        for (CustomerEntity customerEntity : customerEntities) {
            customerEntity.setPointsForCurrentMonth(BigDecimal.ZERO);
        }

        System.out.println("*******  Point of the month reset!  *******");
    }

    @Schedule(month = "*", dayOfMonth = "1", info = "leaderboardPrizes")
    public void giveLeaderBoardPrizes() throws CreateNewDiscountCodeException, InputDataValidationException {
        List<CustomerEntity> allCustomers = customerEntityControllerLocal.retireveAllCustomersOrderedByPoints();
        //List is sorted by descending order
        //Position 1 and 2 (index 0 to 1): 20%, 3 to 4(index 2 to 3): 15%,  5 to 6(index 4 to 5): 10%, the rest: 5%
        List<Long> twentyPercentList = new ArrayList<>();
        List<Long> fifteenPercentList = new ArrayList<>();
        List<Long> tenPercentList = new ArrayList<>();
        List<Long> fivePercentList = new ArrayList<>();
        for (int i = 0; i < allCustomers.size(); i++) {
            if (i <= 1) { //20%
                twentyPercentList.add(allCustomers.get(i).getCustomerId());
            } else if (i > 1 && i <= 3) { //15%
                fifteenPercentList.add(allCustomers.get(i).getCustomerId());
            } else if (i > 3 && i <= 5) { //10%
                tenPercentList.add(allCustomers.get(i).getCustomerId());
            } else { //5%
                fivePercentList.add(allCustomers.get(i).getCustomerId());
            }
        }
        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DAY_OF_YEAR, 14);
        Date endDate = cal.getTime();
        if (twentyPercentList.size() > 0) {
            discountCodeEntityControllerLocal.createNewDiscountCode(new DiscountCodeEntity(startDate, endDate, twentyPercentList.size(), CryptographicHelper.getInstance().generateRandomString(6), DiscountCodeTypeEnum.PERCENTAGE, new BigDecimal(20)),
                     twentyPercentList, null);
        }
        if (fifteenPercentList.size() > 0) {
            discountCodeEntityControllerLocal.createNewDiscountCode(new DiscountCodeEntity(startDate, endDate, fifteenPercentList.size(), CryptographicHelper.getInstance().generateRandomString(6), DiscountCodeTypeEnum.PERCENTAGE, new BigDecimal(15)),
                     fifteenPercentList, null);
        }
        if (tenPercentList.size() > 0) {
            discountCodeEntityControllerLocal.createNewDiscountCode(new DiscountCodeEntity(startDate, endDate, tenPercentList.size(), CryptographicHelper.getInstance().generateRandomString(6), DiscountCodeTypeEnum.PERCENTAGE, new BigDecimal(10)),
                     tenPercentList, null);
        }
        if (fivePercentList.size() > 0) {
            discountCodeEntityControllerLocal.createNewDiscountCode(new DiscountCodeEntity(startDate, endDate, fivePercentList.size(), CryptographicHelper.getInstance().generateRandomString(6), DiscountCodeTypeEnum.PERCENTAGE, new BigDecimal(5)),
                     fivePercentList, null);
        }
        resetPointOfTheMonth();
    }

}
