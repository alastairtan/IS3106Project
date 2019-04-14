/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.ReviewEntity;


public class ReplyToStaffReplyRsp {
    
    private Long customerReplyId;

    public ReplyToStaffReplyRsp() {
    }

    public ReplyToStaffReplyRsp(Long customerReplyId) {
        this.customerReplyId = customerReplyId;
    }

    public Long getCustomerReplyId() {
        return customerReplyId;
    }

    public void setCustomerReplyId(Long customerReplyId) {
        this.customerReplyId = customerReplyId;
    }

  

    
    
}
