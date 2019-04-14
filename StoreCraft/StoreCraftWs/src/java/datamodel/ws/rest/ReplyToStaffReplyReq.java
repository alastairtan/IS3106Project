/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws.rest;

import entity.ReviewEntity;


public class ReplyToStaffReplyReq {
    
    private Long staffReplyId;
    private ReviewEntity customerReply;
    private Long customerId;

    public ReplyToStaffReplyReq() {
    }

    public ReplyToStaffReplyReq(Long staffReplyId, ReviewEntity customerReply, Long customerId) {
        this.staffReplyId = staffReplyId;
        this.customerReply = customerReply;
        this.customerId = customerId;
    }

    public Long getStaffReplyId() {
        return staffReplyId;
    }

    public void setStaffReplyId(Long staffReplyId) {
        this.staffReplyId = staffReplyId;
    }

    public ReviewEntity getCustomerReply() {
        return customerReply;
    }

    public void setCustomerReply(ReviewEntity customerReply) {
        this.customerReply = customerReply;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    

    
    
}
