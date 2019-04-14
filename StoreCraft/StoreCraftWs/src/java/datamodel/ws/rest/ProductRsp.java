/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.ProductEntity;

/**
 *
 * @author Win Phong
 */
public class ProductRsp {

    private ProductEntity productEntity;
    
    public ProductRsp() 
    {
    }

    public ProductRsp(ProductEntity productEntity) {
        this();
        this.productEntity = productEntity;
    }
    

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }
    
}
