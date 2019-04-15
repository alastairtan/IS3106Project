import { Component, OnInit, Input , ViewChild} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Review } from '../review';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { LocalService } from '../local.service';
import { CartItem } from '../cartItem';
import { ReviewService } from '../review.service';
import { Customer } from '../customer';
import { SessionService } from '../session.service';
import { CdkTextareaAutosize } from '@angular/cdk/text-field';

import { ClickEvent } from 'angular-star-rating/angular-star-rating';

@Component({
  selector: 'app-view-product-details',
  templateUrl: './view-product-details.component.html',
  styleUrls: ['./view-product-details.component.css'],
})
export class ViewProductDetailsComponent implements OnInit {

  @ViewChild('autosize') autosize: CdkTextareaAutosize;

  private product: Product;
  private quantity: number = 0;
  private errorMessage: string;
  private productRating: number;
  currentCustomer: Customer;

  //For Updating
  updatedReviewContent: string;
  isEditing: boolean;
  editReviewId: number;
  updatedProductRating: number;
  addToCartMessage: string;
  addToCartMessageClose: boolean;

  //For new Review
  isWriting: boolean;

  constructor(private activatedRoute: ActivatedRoute,
    private productService: ProductService,
    private localService: LocalService,
    private reviewService: ReviewService,
    private sessionService: SessionService) {
      this.isEditing= false;
      this.isWriting= false;
     }

  ngOnInit() {
    let productId = parseInt(this.activatedRoute.snapshot.paramMap.get('productId'));

    this.productService.getProductId(productId).subscribe(response => {
      this.product = response.productEntity
      this.currentCustomer = this.sessionService.getCurrentCustomer();
    }, error => {
      this.errorMessage = error;
    })
  }

  refresh() {
    let productId = parseInt(this.activatedRoute.snapshot.paramMap.get('productId'));

    this.productService.getProductId(productId).subscribe(response => {
      this.product = response.productEntity
      this.currentCustomer = this.sessionService.getCurrentCustomer();
    }, error => {
      this.errorMessage = error;
    })
  }

  format() {
    if (this.product != null) {
      return (new Intl.NumberFormat('en-SG', { style: 'currency', currency: 'SGD' }).format(this.product.unitPrice));
    }
  }

  add() {
    if (this.quantity < 10 && this.quantity < this.product.quantityOnHand) {
      this.quantity = this.quantity + 1;
    }
  }

  minus() {
    if (this.quantity > 0) {
      this.quantity = this.quantity - 1;
    }
  }

  addToCart() {
    console.log("Adding to cart!");

    let newCartItem = new CartItem(0, this.product, this.quantity, this.product.unitPrice, (this.product.unitPrice * this.quantity));

    let cartItems = this.localService.getCart();

    if (cartItems != null) {
      let existingCartItem = cartItems.find((val) => val.productEntity.productId == this.product.productId);

      if (existingCartItem != null) {
        let index = cartItems.indexOf(existingCartItem);

        cartItems[index].quantity += newCartItem.quantity;
      }
      else {
        cartItems.push(newCartItem);
      }
    }
    else {
      cartItems = [];
      cartItems.push(newCartItem);
    }

    if (newCartItem.quantity != 0) {
      this.localService.setCart(cartItems);
      this.addToCartMessageClose = false;
      this.addToCartMessage = "Product succesfully added to cart!"
      setTimeout(() => this.addToCartMessageClose = true, 3000);
    }
    console.log(cartItems);
  }

  editing(custReview: Review) {
    this.isEditing = true;
    this.updatedReviewContent = custReview.content;
    this.editReviewId = custReview.reviewId;
    this.updatedProductRating = custReview.productRating;

  }

  cancelEdit(custReview: Review) {
    this.isEditing = false;
    this.updatedReviewContent = custReview.content;
    this.editReviewId = null;
    this.updatedProductRating = custReview.productRating;
  }

  saveEdit() {

    this.reviewService.updateReview(this.editReviewId, this.updatedReviewContent, this.updatedProductRating).subscribe(response => {
      this.isEditing = false;
      this.refresh();
    }, error => {
      console.log(error)
    })
  }

  onClick = ($event: ClickEvent) => {
    console.log('onClick $event: ', $event);
    this.updatedProductRating = $event.rating;
    console.log("UPDATED: " + this.updatedProductRating);
  };

  writeReview(){
    this.isWriting = true;
  }

  cancelWriteReview(){
    this.isWriting = false;
  }

}
