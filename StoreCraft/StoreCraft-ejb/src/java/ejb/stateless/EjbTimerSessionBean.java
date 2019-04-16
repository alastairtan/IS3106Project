/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.CustomerEntity;
import entity.ProductEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
@Local (EjbTimerSessionBeanLocal.class)

public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal {

    @PersistenceContext(unitName = "StoreCraft-ejbPU")
    private EntityManager entityManager;

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;
    
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    
    
    @Schedule(month="*", info="resetPointOfTheMonth")
    public void resetPointOfTheMonth()
    {
        List<CustomerEntity> customerEntities = customerEntityControllerLocal.retrieveAllCustomer();
        
        for(CustomerEntity customerEntity: customerEntities)
        {
            customerEntity.setPointsForCurrentMonth(BigDecimal.ZERO);
        }
        
        System.out.println("*******  Point of the month reset!  *******");
    }
    
    
    @Schedule(dayOfWeek = "*", info="foo")
    public void foo()
    {
        
    }
    

}
