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

  private cartItems: CartItem[];
  private selectedCartItem: CartItem;
  private totalAmountForTheCart: number;
  private totalQuantityForTheCart: number;
  private promoCode: DiscountCode;
  private infoMessage: string;
  private errorMessage: string;
  private removeMessage: string;
  private removeMessageClose: boolean;
  private infoMessageClose: boolean;

  private customerDiscountCodes: DiscountCode[];
  private currentCustomer: Customer;

  constructor(private localService: LocalService,
    private saleTransactionService: SaleTransactionService,
    private sessionService: SessionService,
    private customerService: CustomerService,
    private discountCodeService: DiscountCodeService) { }

  ngOnInit() {
    this.cartItems = this.localService.getCart();
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

    this.saleTransactionService.createSaleTransaction(cartItems).subscribe(response => {
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

  applyDiscountCode(){
    if (this.promoCode != null){
      if (this.promoCode.productEntities == null || this.promoCode.productEntities.length == 0){
        this.applyDiscountCodeToShoppingCart();
      }

    }
  }


  applyDiscountCodeToShoppingCart(){
    let discountCodeType = this.promoCode.discountCodeTypeEnum.toString();
    if (discountCodeType == 'FLAT'){
      let discountAmount = this.promoCode.discountAmountOrRate;
      
    } else if (discountCodeType == 'PERCENTAGE'){
      let discountRate = this.promoCode.discountAmountOrRate;
    }
  }


  getDiscountCodeMessage(discountCodeId: number): string {
    //If discount code is for some products, return a string of which products
    //If discount code is for all products, return a string of "ALL"
    let message: string = "(For: "

    let discountCodeToProcess: DiscountCode = this.customerDiscountCodes.find(dc =>
      dc.discountCodeId == discountCodeId);

    if (discountCodeToProcess.productEntities == null || discountCodeToProcess.productEntities.length == 0) {
      message = "(For Entire Cart)";
      return message;
    }

    let discountCodeProducts: Product[] = discountCodeToProcess.productEntities;

    //for each discount code product, check if product exists in shopping cart
    discountCodeProducts.forEach(dcProduct => {

      if (this.isDiscountCodeProductInCart(dcProduct.productId)) {
        message += dcProduct.name + ","
      }


    })

    message = message.slice(0, message.length - 1);
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
      let startDate:Date = new Date(discountCode.startDate);
      if (endDate > now && startDate < now) {
        filteredDiscountCodes.push(discountCode)
      }
    })
    return filteredDiscountCodes;
  }

  filterOutUnavailableDiscountCodes(discountCodes: DiscountCode[]): DiscountCode[]{
      let filteredDiscountCodes = [];
      discountCodes.forEach(discountCode => {
        if (discountCode.numAvailable > 0){
          filteredDiscountCodes.push(discountCode);
        }
      })
      return filteredDiscountCodes;
  }

  filterDiscountCodes(discountCodes : DiscountCode[]): DiscountCode[]{
    return this.filterOutUnavailableDiscountCodes(this.filterValidDiscountCodes(this.filterOutInapplicableDiscountCodes(discountCodes)));
  }
}
