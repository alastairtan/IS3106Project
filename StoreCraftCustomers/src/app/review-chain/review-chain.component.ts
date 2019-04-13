import { Component, OnInit, Input } from '@angular/core';
import { Review } from '../review';
import { ReviewService } from '../review.service';

@Component({
  selector: 'app-review-chain',
  templateUrl: './review-chain.component.html',
  styleUrls: ['./review-chain.component.css']
})
export class ReviewChainComponent implements OnInit {

  @Input()
  rootReviewId: number;

  reviewChain: Review[]

  constructor(
    private reviewService: ReviewService
  ) { 
  }

  ngOnInit() {
    this.reviewService.getReviewChain(this.rootReviewId).subscribe(response => {
      this.reviewChain = response.reviewEntities;
    })
  }

}
