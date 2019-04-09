/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.ProductEntity;
import java.util.List;

/**
 *
 * @author shawn
 */
public class RetrieveProdByCategoryRsp {
    
    private List<ProductEntity> productEntities;

    public RetrieveProdByCategoryRsp() {
    }

    public RetrieveProdByCategoryRsp(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }

    
}
