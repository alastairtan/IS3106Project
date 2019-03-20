/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 *
 * @author shawn
 */
@Stateless
@Local (EjbTimerSessionBeanLocal.class)
public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
