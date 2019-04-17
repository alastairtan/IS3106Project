import { Component, OnInit, Input } from '@angular/core';
import { ReviewService } from '../review.service';
import { Review } from '../review';


@Component({
  selector: 'app-user-review-table',
  templateUrl: './user-review-table.component.html',
  styleUrls: ['./user-review-table.component.css']
})
export class UserReviewTableComponent implements OnInit {

  @Input()
  customerId: number;

  reviews: Review[];

  columnsToDisplay =  ['Date', 'Product', 'Rating', 'Content'];

  constructor(private reviewService: ReviewService) { }

  ngOnInit() {
    this.getCustomerReviews();
  }

  getCustomerReviews() {
    if (this.customerId != null) {
      this.reviewService.getCustomerReviews(this.customerId).subscribe(response => {
        this.reviews = response.reviewEntities;
        console.log(this.reviews);
      })
    }
  }
}
