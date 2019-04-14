/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.SaleTransactionEntity;

/**
 *
 * @author Win Phong
 */
public class SaleTransactionReq {
    
    private SaleTransactionEntity saleTransactionEntity;

    public SaleTransactionReq() {
    }

    public SaleTransactionReq(SaleTransactionEntity saleTransactionEntity) {
        this.saleTransactionEntity = saleTransactionEntity;
    }
    
    

    public SaleTransactionEntity getSaleTransactionEntity() {
        return saleTransactionEntity;
    }

    public void setSaleTransactionEntity(SaleTransactionEntity saleTransactionEntity) {
        this.saleTransactionEntity = saleTransactionEntity;
    }
}
