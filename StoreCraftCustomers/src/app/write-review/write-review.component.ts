import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ReviewService} from '../review.service';
import {SessionService} from '../session.service';
import {ClickEvent} from 'angular-star-rating/angular-star-rating';

@Component({
  selector: 'app-write-review',
  templateUrl: './write-review.component.html',
  styleUrls: ['./write-review.component.css']
})
export class WriteReviewComponent implements OnInit {

  @Input()
  productId: number;

  @Input()
  show: boolean;

  @Output()
  submitted = new EventEmitter();

  @Output()
  closed = new EventEmitter();

  newReviewContent: string;
  customerId: number;
  newProductRating: number;

  constructor(
    private reviewService: ReviewService,
    private sessionService: SessionService
  ) {
  }

  ngOnInit() {
  }

  createReview() {
    this.reviewService.createReview(this.sessionService.getCurrentCustomer().customerId,
      this.newReviewContent, this.newProductRating, this.productId).subscribe(response => {
      console.log(response.reviewEntity);
      this.show = false;
      this.submitted.emit();
    }, error => {
      console.log(error);
    });
  }

  onClick = ($event: ClickEvent) => {
    console.log('onClick $event: ', $event);
    this.newProductRating = $event.rating;
  }


  close() {
    this.show = false;
    this.closed.emit();
    this.newReviewContent = null;
    this.newProductRating = null;
  }

}
