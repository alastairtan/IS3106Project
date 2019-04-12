/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import ejb.stateless.CustomerEntityControllerLocal;
import entity.CustomerEntity;
import entity.SaleTransactionEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Win Phong
 */
public class SaleTransactionRsp {
    
    private SaleTransactionEntity saleTransactionEntity;
    private List<SaleTransactionEntity> saleTransactionEntities;
    private CustomerEntity customerEntity;
    
    public SaleTransactionRsp() throws Exception {
    }
    
    public SaleTransactionRsp(SaleTransactionEntity saleTransactionEntity)
    {
        this.saleTransactionEntity = saleTransactionEntity;
    }
    
    public SaleTransactionRsp(List<SaleTransactionEntity> saleTransactionEntities)
    {
        this.saleTransactionEntities = saleTransactionEntities;

    }

    public List<SaleTransactionEntity> getSaleTransactionEntities() {
        return saleTransactionEntities;
    }

    public void setSaleTransactionEntities(List<SaleTransactionEntity> saleTransactionEntities) {
        this.saleTransactionEntities = saleTransactionEntities;
    }

    public SaleTransactionEntity getSaleTransactionEntity() {
        return saleTransactionEntity;
    }

    public void setSaleTransactionEntity(SaleTransactionEntity saleTransactionEntity) {
        this.saleTransactionEntity = saleTransactionEntity;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }
}
