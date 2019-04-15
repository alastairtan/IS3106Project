/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.SaleTransactionEntity;
import java.util.List;

/**
 *
 * @author Win Phong
 */
public class SaleTransactionRsp {
    
    private SaleTransactionEntity saleTransactionEntity;
    private List<SaleTransactionEntity> saleTransactionEntities;
    
    public SaleTransactionRsp() {
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
}
