import { Component, OnInit } from '@angular/core';
import { CartItem } from '../cartItem';
import { LocalService } from '../local.service';
import { SaleTransactionService } from '../sale-transaction.service';
import { SessionService } from '../session.service';
import { UserProfileComponent } from '../user-profile/user-profile.component';
import { CustomerService } from '../customer.service';
import { refreshDescendantViews } from '@angular/core/src/render3/instructions';
import { DiscountCode } from '../discount-code';
import { DiscountCodeService } from '../discount-code.service';
import { Customer } from '../customer';
import { Product } from '../product';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {

  //Initial Load
  private cartItems: CartItem[];
  private totalAmountForTheCart: number;
  private totalQuantityForTheCart: number;
  private customerDiscountCodes: DiscountCode[];
  private currentCustomer: Customer;

  //Checkout//
  private currentSelectedDiscountCode: DiscountCode;
  private discountCodeToApply: DiscountCode;
  private pointsToUse: number
  
  //Messages
  private infoMessage: string;
  private errorMessage: string;
  private removeMessage: string;
  private removeMessageClose: boolean;
  private infoMessageClose: boolean;
  private errorMessageClose: boolean;

  //For cart discount
  applyingDiscountForCart: boolean;
  flatDiscountAmountForCart: number;
  rateDiscountAmountForCart: number;

  //For indiv products discount
  cartItemsCopy: CartItem[] //loaded initially, used to display new price, modify this to show discounted prices
  applyingDiscountForItems: boolean;
  totalAmountDiscounted: number;


  constructor(private localService: LocalService,
    private saleTransactionService: SaleTransactionService,
    private sessionService: SessionService,
    private customerService: CustomerService,
    private discountCodeService: DiscountCodeService) {
    this.applyingDiscountForCart = false;
  }

  ngOnInit() {
    this.cartItems = this.localService.getCart();
    this.cartItemsCopy = JSON.parse(JSON.stringify(this.cartItems));
    this.loadCustomerData();
    // this.cartItems.filter() --> to remove item that is no longer available (e.g. deleted)
    if (this.cartItems != null) {
      this.totalAmountForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
      this.totalQuantityForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.quantity, 0);
    }
  }

  retrieveDiscountCodesForCustomer(customerId: number) {
    this.discountCodeService.retrieveDiscountCodesForCustomer(customerId).subscribe(response => {
      //console.log(response.discountCodeEntities);
      this.customerDiscountCodes = this.filterDiscountCodes(response.discountCodeEntities);
    })
  }

  loadCustomerData() {
    this.retrieveDiscountCodesForCustomer(this.sessionService.getCurrentCustomer().customerId)
    this.currentCustomer = this.sessionService.getCurrentCustomer();
  }

  removeFromCart(cartItem: CartItem) {
    this.cartItems.splice(this.cartItems.indexOf(cartItem), 1);
    this.localService.setCart(this.cartItems);
    this.removeMessage = `Product ${cartItem.productEntity.name} has been removed from cart`;
    this.removeMessageClose = false;
    this.totalAmountForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
    this.totalQuantityForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.quantity, 0);
    this.cartItems = this.localService.getCart();

    setTimeout(() => this.removeMessageClose = true, 3000);
  }

  format(currency: number) {
    return (new Intl.NumberFormat('en-SG', { style: 'currency', currency: 'SGD' }).format(currency));
  }

  checkout(cartItems: CartItem[]) {

    this.saleTransactionService.createSaleTransaction(cartItems, this.discountCodeToApply, this.pointsToUse).subscribe(response => {
      console.log(response);
      this.removeMessageClose = true;
      this.infoMessageClose = false;
      this.infoMessage = "Thank you for shopping with us!";
      this.localService.clearCart();
      this.cartItems = this.localService.getCart();

      this.updateCustomer(response);

      // setTimeout(() => this.infoMessageClose = true, 3000);
    }
    ), (error: string) => {
      this.errorMessage = error;
      console.log("Error 500?? " + error);
    }
  }

  updateCustomer(response) {
    console.log("Enum: " + response.saleTransactionEntity.customerEntity.membershipTierEnum);

    let tierInfo = this.customerService.setTierInfo(response.saleTransactionEntity.customerEntity.membershipTierEnum);

    console.log("Test");
    console.log(tierInfo.tierMessage);
    console.log(tierInfo.tierUrl);
    response.saleTransactionEntity.customerEntity.tierMessage = tierInfo.tierMessage;
    response.saleTransactionEntity.customerEntity.tierUrl = tierInfo.tierUrl;

    this.sessionService.setCurrentCustomer(response.saleTransactionEntity.customerEntity);
  }

  add(cartItem: CartItem) {
    cartItem.quantity = cartItem.quantity + 1;
    cartItem.subTotal = cartItem.quantity * cartItem.unitPrice;
    this.refresh();
  }

  minus(cartItem: CartItem) {
    cartItem.quantity = cartItem.quantity - 1;
    cartItem.subTotal = cartItem.quantity * cartItem.unitPrice;
    this.refresh();
  }

  refresh() {
    this.localService.setCart(this.cartItems);
    this.cartItems = this.localService.getCart();
    this.totalAmountForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
    this.totalQuantityForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.quantity, 0);
  }

  resetSelected(){
    this.rateDiscountAmountForCart = null;
    this.flatDiscountAmountForCart = null;
    this.applyingDiscountForCart = false;
    this.applyingDiscountForItems = false;
  }

  applyDiscountCode() {
    this.discountCodeToApply = JSON.parse(JSON.stringify(this.currentSelectedDiscountCode));
    if (this.discountCodeToApply != null) {
      console.log("APPLY 1");
      if (this.discountCodeToApply.productEntities == null || this.discountCodeToApply.productEntities.length == 0) {
        console.log("APPLY 2");
        this.applyDiscountCodeToShoppingCart();
      } else { //for some products
        this.cartItemsCopy = JSON.parse(JSON.stringify(this.cartItems));
        this.applyDiscountCodeToCartItems(this.discountCodeToApply, this.cartItemsCopy);
      }
    }
  }

  applyDiscountCodeToCartItems(discountCode: DiscountCode, cartItems: CartItem[]) {
    let cartItemsToDiscount: CartItem[] = this.getCartItemsToApplyDiscountTo(discountCode, cartItems);

    if (discountCode.discountCodeTypeEnum.toString() == 'FLAT') {
      let discountAmount: number = discountCode.discountAmountOrRate;

      cartItemsToDiscount.forEach(cartItem => {
        if (cartItem.subTotal - discountAmount > 0) {
          cartItem.subTotal = cartItem.subTotal - discountAmount;
        } 
      })
    } else if (discountCode.discountCodeTypeEnum.toString() == 'PERCENTAGE'){
      let discountRate: number = discountCode.discountAmountOrRate;

      cartItemsToDiscount.forEach(cartItem => {
        let discountBy: number = cartItem.subTotal * discountRate;

        cartItem.subTotal = cartItem.subTotal - discountBy;
      })
    }
    this.totalAmountDiscounted = this.cartItemsCopy.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
    this.applyingDiscountForItems = true;
  }

  getCartItemsToApplyDiscountTo(discountCode: DiscountCode, cartItems: CartItem[]) {
    let cartItemsToDiscount: CartItem[] = [];
    let discountCodeProducts = discountCode.productEntities;
    //console.log(discountCodeProducts);
    discountCodeProducts.forEach(dcProduct => {
      cartItems.forEach(cartItem => {
        if (dcProduct.productId == cartItem.productEntity.productId) {
          cartItemsToDiscount.push(cartItem);
        }
      })
    })
    //console.log(cartItemsToDiscount);
    return cartItemsToDiscount;
  }


  applyDiscountCodeToShoppingCart() {
    let discountCodeType = this.discountCodeToApply.discountCodeTypeEnum.toString();
    console.log(discountCodeType);
    if (discountCodeType == 'FLAT') {
      this.rateDiscountAmountForCart = null;
      this.flatDiscountAmountForCart = this.discountCodeToApply.discountAmountOrRate;
    } else if (discountCodeType == 'PERCENTAGE') {
      this.flatDiscountAmountForCart = null;
      this.rateDiscountAmountForCart = this.discountCodeToApply.discountAmountOrRate; //eg 5%
    }
    this.applyingDiscountForCart = true;
  }

  //DISCOUNT CODE FILTERING

  getDiscountCodeMessage(discountCodeId: number): string {
    //If discount code is for some products, return a string of which products
    //If discount code is for all products, return a string of "ALL"
    let message: string = "(";

    let discountCodeToProcess: DiscountCode = this.customerDiscountCodes.find(dc =>
      dc.discountCodeId == discountCodeId);

    let discountCodeAmount = discountCodeToProcess.discountAmountOrRate;
    if (discountCodeToProcess.discountCodeTypeEnum.toString() == 'FLAT') {
      message += this.format(discountCodeAmount) + " Off " //e.g. $5
    } else if (discountCodeToProcess.discountCodeTypeEnum.toString() == 'PERCENTAGE') {
      message += discountCodeAmount + "% Off ";
    }

    if (discountCodeToProcess.productEntities == null || discountCodeToProcess.productEntities.length == 0) {
      message += "Entire Cart)";
      return message;
    }

    let discountCodeProducts: Product[] = discountCodeToProcess.productEntities;

    //for each discount code product, check if product exists in shopping cart
    discountCodeProducts.forEach(dcProduct => {

      if (this.isDiscountCodeProductInCart(dcProduct.productId)) {
        message += dcProduct.name + ", "
      }


    })

    message = message.slice(0, message.length - 2);
    message += ")";

    return message;
  }

  isDiscountCodeProductInCart(dcProductId: number): boolean {
    let foundCartItem: CartItem = this.cartItems.find(cartItem => cartItem.productEntity.productId == dcProductId);

    if (foundCartItem != null) {
      return true;
    } else {
      return false;
    }
  }

  //applicable if all of its products are in cart, or it is meant for all products
  isDiscountCodeApplicable(discountCode: DiscountCode): boolean {
    if (discountCode.productEntities == null || discountCode.productEntities.length == 0) {
      return true;
    }
    let discountCodeProducts: Product[] = discountCode.productEntities;
    //console.log(discountCodeProducts);
    //for each discount code product, check if product exists in shopping cart
    for (let i = 0; i < discountCodeProducts.length; i++) {
      if (this.isDiscountCodeProductInCart(discountCodeProducts[i].productId)) {
        return true;
      }
    }

    return false;

  }

  filterOutInapplicableDiscountCodes(discountCodes: DiscountCode[]): DiscountCode[] {
    //console.log(discountCodes);
    if (discountCodes != null) {
      let filteredDiscountCodes: DiscountCode[] = discountCodes.filter(discountCode =>
        this.isDiscountCodeApplicable(discountCode)
      )
      //console.log(filteredDiscountCodes);
      return filteredDiscountCodes;
    }

  }

  filterValidDiscountCodes(discountCodes: DiscountCode[]): DiscountCode[] {
    let now: Date = new Date();
    let filteredDiscountCodes = [];
    discountCodes.forEach(discountCode => {
      let endDate: Date = new Date(discountCode.endDate);
      let startDate: Date = new Date(discountCode.startDate);
      if (endDate > now && startDate < now) {
        filteredDiscountCodes.push(discountCode)
      }
    })
    return filteredDiscountCodes;
  }

  filterOutUnavailableDiscountCodes(discountCodes: DiscountCode[]): DiscountCode[] {
    let filteredDiscountCodes = [];
    discountCodes.forEach(discountCode => {
      if (discountCode.numAvailable > 0) {
        filteredDiscountCodes.push(discountCode);
      }
    })
    return filteredDiscountCodes;
  }

  filterDiscountCodes(discountCodes: DiscountCode[]): DiscountCode[] {
    return this.filterOutUnavailableDiscountCodes(this.filterValidDiscountCodes(this.filterOutInapplicableDiscountCodes(discountCodes)));
  }


}
