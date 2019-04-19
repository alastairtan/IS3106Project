import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {MatDialog} from '@angular/material';
import {Review} from '../review';
import {Product} from '../product';
import {ProductService} from '../product.service';
import {LocalService} from '../local.service';
import {CartItem} from '../cartItem';
import {ReviewService} from '../review.service';
import {Customer} from '../customer';
import {SessionService} from '../session.service';
import {CdkTextareaAutosize} from '@angular/cdk/text-field';
import {LoginDialogComponent} from '../login-dialog/login-dialog.component';
import {ClickEvent} from 'angular-star-rating/angular-star-rating';
import {ScavengerHuntService} from '../scavenger-hunt.service';
import {CustomerService} from '../customer.service';

@Component({
  selector: 'app-view-product-details',
  templateUrl: './view-product-details.component.html',
  styleUrls: ['./view-product-details.component.css'],
})
export class ViewProductDetailsComponent implements OnInit {

  @ViewChild('autosize') autosize: CdkTextareaAutosize;

  private product: Product;
  private quantity = 0;
  private errorMessage: string;
  private averageRating: number;
  private numberOfRatings: number;
  currentCustomer: Customer;
  private existInCart : boolean;

  // For Updating
  updatedReviewContent: string;
  isEditing: boolean;
  editReviewId: number;
  updatedProductRating: number;
  addToCartMessage: string;
  addToCartMessageClose: boolean;

  // For new Review
  isWriting: boolean;

  // For scavenger hunt
  canCustomerWin: boolean;
  prizeClaimMessage: string;

  constructor(private activatedRoute: ActivatedRoute,
              private productService: ProductService,
              private localService: LocalService,
              private reviewService: ReviewService,
              private sessionService: SessionService,
              private scavengerHuntService: ScavengerHuntService,
              private customerService: CustomerService,
              private loginDialog: MatDialog) {
    this.isEditing = false;
    this.isWriting = false;
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.refresh();
    });
  }

  refresh() {
    // tslint:disable-next-line:radix
    const productId = parseInt(this.activatedRoute.snapshot.paramMap.get('productId'));

    this.productService.getProductId(productId).subscribe(response => {
      this.product = response.productEntity
      this.checkExistInCart();
      this.currentCustomer = this.sessionService.getCurrentCustomer();
      this.scavengerHuntService.checkIfCustomerHasWonToday(
        this.currentCustomer.customerId).subscribe(response => {
        this.canCustomerWin = !response.hasCustomerWonToday;
      });
      this.productService.getRatingInfoForProduct(this.product.productId).subscribe(response => {
          this.averageRating = response.result[0];
          this.numberOfRatings = response.result[1];
      });
    }, error => {
      this.errorMessage = error;
    });
  }

  checkExistInCart() {
    let cartItems = this.localService.getCart();
    if (cartItems != null) {
      let existingCartItem = cartItems.find((cartItem) => cartItem.productEntity.productId == this.product.productId);
      if (existingCartItem != null) {
        this.existInCart = true;
      }
    }
  }

  format() {
    if (this.product != null) {
      return (new Intl.NumberFormat('en-SG', {style: 'currency', currency: 'SGD'}).format(this.product.unitPrice));
    }
  }

  add() {
    if (this.quantity < this.product.quantityOnHand) {
      this.quantity = this.quantity + 1;
    }
  }

  minus() {
    if (this.quantity > 0) {
      this.quantity = this.quantity - 1;
    }
  }

  addToCart() {
    console.log('Adding to cart!');

    const newCartItem = new CartItem(0, this.product, this.quantity, this.product.unitPrice, (this.product.unitPrice * this.quantity));

    let cartItems = this.localService.getCart();

    if (cartItems != null) {
      const existingCartItem = cartItems.find((val) => val.productEntity.productId == this.product.productId);

      if (existingCartItem != null) {
        const index = cartItems.indexOf(existingCartItem);

        cartItems[index].quantity += newCartItem.quantity;
      } else {
        cartItems.push(newCartItem);
        this.existInCart = true;
      }
    } else {
      cartItems = [];
      cartItems.push(newCartItem);
      this.existInCart = true;
    }

    if (newCartItem.quantity != 0) {
      this.localService.setCart(cartItems);
      this.addToCartMessageClose = false;
      this.addToCartMessage = 'Product succesfully added to cart!';
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
      console.log(error);
    });
  }

  onClick = ($event: ClickEvent) => {
    console.log('onClick $event: ', $event);
    this.updatedProductRating = $event.rating;
    console.log('UPDATED: ' + this.updatedProductRating);
  }

  writeReview() {
    this.isWriting = true;
  }

  cancelWriteReview() {
    this.isWriting = false;
  }

  openDialog(): void {
    const dialogRef = this.loginDialog.open(LoginDialogComponent, {});

    dialogRef.afterClosed().subscribe(result => {
      this.currentCustomer = this.sessionService.getCurrentCustomer();
    });
  }

  claimScavengerHuntPrize(): void {
    this.scavengerHuntService.claimScavengerHuntPrize(this.currentCustomer.customerId).subscribe(
      response => {
        // Prize updated in database
        const customerEntity = response.customerEntity;
        console.log('String ' + JSON.stringify(customerEntity));
        console.log('Membership : ' + customerEntity.membershipTierEnum);

        const tierInfo = this.customerService.setTierInfo(customerEntity.membershipTierEnum);

        customerEntity.tierMessage = tierInfo.tierMessage;
        customerEntity.tierUrl = tierInfo.tierUrl;

        this.sessionService.setCurrentCustomer(customerEntity);
        this.refresh();

        this.scavengerHuntService.retrieveScavengerHuntForTheDay().subscribe(
          // tslint:disable-next-line:no-shadowed-variable
          response => {

            console.log(response.scavengerHuntEntity.rewardTypeEnum);

            if (response.scavengerHuntEntity.rewardTypeEnum == 'DISCOUNT_CODE_FLAT') {
              this.prizeClaimMessage = 'You have won a flat rate discount code! Check your profile to see the discount code!';
            } else if (response.scavengerHuntEntity.rewardTypeEnum == 'DISCOUNT_CODE_PERCENTAGE') {
              this.prizeClaimMessage = 'You have won a percentage discount code from the scavenger hunt! Check your profile to see the discount code!';
            } else if (response.scavengerHuntEntity.rewardTypeEnum == 'POINTS') {
              this.prizeClaimMessage = 'You have won points from the scavenger hunt! Check your profile to see the discount code!';
            }
            setTimeout(() => this.prizeClaimMessage = '', 6000);
          }
        );
      }
    );
  }
}
