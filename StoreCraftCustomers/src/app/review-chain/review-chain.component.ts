import { Component, OnInit, Input } from '@angular/core';
import { Review } from '../review';
import { ReviewService } from '../review.service';
import { Customer } from '../customer';
import { SessionService } from '../session.service';


@Component({
  selector: 'app-review-chain',
  templateUrl: './review-chain.component.html',
  styleUrls: ['./review-chain.component.css']
})
export class ReviewChainComponent implements OnInit {

  @Input()
  rootReviewId: number;

  //Initial Load
  reviewChain: Review[];
  currentCustomer: Customer;
  //************

  //For Replying
  isReplying: boolean;
  staffReplyToReplyTo: Review;
  replyContent: string;
  //************
  
  //For Editing
  isEditing: boolean;
  updatedReplyContent: string; 
  editReviewId: number;
  //*********** 




  constructor(
    private reviewService: ReviewService,
    private sessionService: SessionService
  ) {
    this.isReplying = false;
    this.isEditing = false;
  }

  ngOnInit() {
    this.getReviewChain();
  }

  getReviewChain() {
    this.reviewService.getReviewChain(this.rootReviewId).subscribe(response => {
      this.reviewChain = response.reviewEntities;
      this.currentCustomer = this.sessionService.getCurrentCustomer();
    })
  }

  replying(staffReply: Review) {
    this.isReplying = true;
    this.staffReplyToReplyTo = staffReply;
  }

  cancelReply() {
    this.isReplying = false;
    this.replyContent = null;
  }

  editing(custReply: Review) {
    this.isEditing = true;
    this.updatedReplyContent = custReply.content;
    this.editReviewId = custReply.reviewId;

  }

  cancelEdit(custReply: Review){
    this.isEditing = false;
    this.updatedReplyContent = custReply.content;
    this.editReviewId = null;
  }


  reply() {
    console.log("HELLO");
    console.log(this.staffReplyToReplyTo.reviewId);
    console.log(this.currentCustomer);
    let customerReply: Review = new Review();
    customerReply.content = this.replyContent;
    customerReply.reviewDate = new Date();
    let customerId = this.sessionService.getCurrentCustomer().customerId;
    this.reviewService.replyToStaffReply(customerReply, this.staffReplyToReplyTo, customerId).subscribe(response => {
      //console.log(response.customerReplyId);
      this.isReplying = false;
      this.getReviewChain();
    },
      error => {
        console.log(error);
      })
  }

  saveEdit(){

    this.reviewService.updateReview(this.editReviewId, this.updatedReplyContent, null).subscribe(response => {
      this.isEditing=false;
      this.getReviewChain();
    }, error => {
      console.log(error)
    })
  }

  test(){
    console.log(this.staffReplyToReplyTo.reviewId);
  }

}
