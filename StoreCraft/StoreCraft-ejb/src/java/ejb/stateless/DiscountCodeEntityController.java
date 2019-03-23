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
@Local (DiscountCodeEntityControllerLocal.class)
public class DiscountCodeEntityController implements DiscountCodeEntityControllerLocal {

    
}
