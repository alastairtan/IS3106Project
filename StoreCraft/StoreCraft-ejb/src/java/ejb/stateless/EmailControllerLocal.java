/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.SaleTransactionEntity;
import java.util.concurrent.Future;
import javax.ejb.Local;

/**
 *
 * @author shawn
 */
@Local
public interface EmailControllerLocal {

    public Boolean emailCheckoutNotificationSync(SaleTransactionEntity saleTransactionEntity, String fromEmailAddress, String toEmailAddress);

    public Future<Boolean> emailCheckoutNotificationAsync(SaleTransactionEntity saleTransactionEntity, String fromEmailAddress, String toEmailAddress) throws InterruptedException;
    
}
