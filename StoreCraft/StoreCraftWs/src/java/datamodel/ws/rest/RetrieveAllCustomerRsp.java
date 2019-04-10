/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.CustomerEntity;
import java.util.List;

/**
 *
 * @author Win Phong
 */
public class RetrieveAllCustomerRsp {
    
    private List<CustomerEntity> customerEntities;
    
    public RetrieveAllCustomerRsp() {
    }
    
    public RetrieveAllCustomerRsp(List<CustomerEntity> customerEntities) {
       this.customerEntities = customerEntities;
    }

    public List<CustomerEntity> getCustomerEntities() {
        return customerEntities;
    }

    public void setCustomerEntities(List<CustomerEntity> customerEntities) {
        this.customerEntities = customerEntities;
    }
    
    
}
