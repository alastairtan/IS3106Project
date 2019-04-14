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

  reviewChain: Review[];

  currentCustomer: Customer;

  isReplying: boolean;

  replyContent: string;

  staffReplyToReplyTo: Review;

  constructor(
    private reviewService: ReviewService,
    private sessionService: SessionService
  ) {
    this.isReplying = false;
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
    console.log(this.replyContent);
  }

  cancelReply() {
    this.isReplying = false;
  }

  reply() {
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

}
