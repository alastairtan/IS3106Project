/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.SaleTransactionEntity;
import java.util.concurrent.Future;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import util.email.EmailManager;

/**
 *
 * @author shawn
 */
@Stateless
public class EmailController implements EmailControllerLocal {

    private final String UNIX_USERNAME = "<REPLACE_WITH_SOC_EMAIL_USERNAME>";
    private final String UNIX_PASSWORD = "<REPLACE_WITH_SOC_EMAIL_PASSWORD>";
    

    @Override
    public Boolean emailCheckoutNotificationSync(SaleTransactionEntity saleTransactionEntity, String fromEmailAddress, String toEmailAddress)
    {
        EmailManager emailManager = new EmailManager(UNIX_USERNAME, UNIX_PASSWORD);
        Boolean result = emailManager.emailCheckoutNotification(saleTransactionEntity, fromEmailAddress, toEmailAddress);
        
        return result;
    } 
    
    
    @Asynchronous
    @Override
    public Future<Boolean> emailCheckoutNotificationAsync(SaleTransactionEntity saleTransactionEntity, String fromEmailAddress, String toEmailAddress) throws InterruptedException
    {
        EmailManager emailManager = new EmailManager(UNIX_USERNAME, UNIX_PASSWORD);
        Boolean result = emailManager.emailCheckoutNotification(saleTransactionEntity, fromEmailAddress, toEmailAddress);
        
        return new AsyncResult<>(result);
    }
}
