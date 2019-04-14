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

  reviewChain: Review[]

  private customerId: number;

  constructor(
    private reviewService: ReviewService,
    private sessionService: SessionService
  ) { 
  }

  ngOnInit() {
    this.reviewService.getReviewChain(this.rootReviewId).subscribe(response => {
      this.reviewChain = response.reviewEntities;
    })
    // Need to set condition to only show the reply button if the logged in customer is the same
    // one as the one who wrote the review in the first place. But the JSON does not contain enough information
    // to check for that
    this.customerId = this.sessionService.getCurrentCustomer().customerId;
  }

  reply() {
    this.reviewService
  }

}
