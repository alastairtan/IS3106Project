/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import util.exception.CreateNewDiscountCodeException;
import util.exception.InputDataValidationException;

/**
 *
 * @author shawn
 */
public interface EjbTimerSessionBeanLocal {

    public void giveLeaderBoardPrizes() throws CreateNewDiscountCodeException, InputDataValidationException;

}
