import {Component, OnInit} from '@angular/core';
import {CartItem} from '../cartItem';
import {LocalService} from '../local.service';
import {SaleTransactionService} from '../sale-transaction.service';
import {SessionService} from '../session.service';
import {CustomerService} from '../customer.service';
import {DiscountCode} from '../discount-code';
import {DiscountCodeService} from '../discount-code.service';
import {ProductService} from '../product.service';
import {Customer} from '../customer';
import {Product} from '../product';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {

  // Initial Load
  private cartItems: CartItem[];
  private totalAmountForTheCart: number;
  private totalQuantityForTheCart: number;
  private customerDiscountCodes: DiscountCode[];
  private allDiscountCodes: DiscountCode[];

  // Checkout//
  private currentSelectedDiscountCode: DiscountCode;
  private discountCodeToApply: DiscountCode;
  private pointsToUse: number;

  // Messages
  private infoMessage: string;
  private errorMessage: string;
  private removeMessage: string;
  private removeMessageClose: boolean;
  private infoMessageClose: boolean;
  private errorMessageClose: boolean;
  private updateMessages = [];
  private autoRemoveMessages = [];


  // For cart discount
  applyingDiscountForCart: boolean;
  flatDiscountAmountForCart: number;
  rateDiscountAmountForCart: number;

  // For indiv products discount
  cartItemsCopy: CartItem[]; // loaded initially, used to display new price, modify this to show discounted prices
  applyingDiscountForItems: boolean;
  totalAmountDiscounted: number;

  // For points slider
  currentCustomer: Customer; // initial load as well


  constructor(private localService: LocalService,
              private saleTransactionService: SaleTransactionService,
              private sessionService: SessionService,
              private customerService: CustomerService,
              private discountCodeService: DiscountCodeService,
              private productService: ProductService) {
    this.applyingDiscountForCart = false;
    this.pointsToUse = 0;
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.cartItems = this.localService.getCart();
    // this.cartItems.filter() --> to remove item that is no longer available (e.g. deleted)
    if (this.cartItems != null) {

      let products;

      this.productService.getAllProducts().subscribe(response => {
        products = response.productEntities.filter(product => this.cartItems.find(cartItem =>
          cartItem.productEntity.productId == product.productId) != null);

        console.log('Product');
        console.log(products);
        let cartItemsCopy = JSON.parse(JSON.stringify(this.cartItems));

        this.cartItems.forEach(cartItem => {

          let product = products.find(product => product.productId == cartItem.productEntity.productId);
          let productQtyOnHand = product.quantityOnHand;

          console.log('How many times');
          console.log(product);

          if (cartItem.quantity > productQtyOnHand) {
            console.log('**** if');
            if (productQtyOnHand != 0) {
              cartItem.quantity = productQtyOnHand;
              cartItem.subTotal = productQtyOnHand * cartItem.unitPrice;
              cartItem.productEntity.quantityOnHand = productQtyOnHand;
              this.totalAmountForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
              this.totalQuantityForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.quantity, 0);
              this.updateMessages.push(`Quantity of product ${product.name} in cart has been adjust to ${productQtyOnHand} due to limited stock`);
              console.log('This is messages: ' + this.updateMessages);
              console.log(this.updateMessages.length);
            } else {
              cartItemsCopy.splice(cartItemsCopy.indexOf(cartItem), 1);
              console.log(this.cartItems);
              console.log(cartItemsCopy);
              this.autoRemoveMessages.push(`Product ${cartItem.productEntity.name} has been removed from cart due to lack of stock`);
              console.log(this.autoRemoveMessages);
              this.totalAmountForTheCart = cartItemsCopy.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
              this.totalQuantityForTheCart = cartItemsCopy.reduce((acc, cartItem) => acc + cartItem.quantity, 0);
            }
          } else {
            console.log('*** else');
            cartItem.productEntity.quantityOnHand = productQtyOnHand;
          }
        });
        if (this.autoRemoveMessages.length > 0) {
          this.localService.setCart(cartItemsCopy);
        } else {
          this.localService.setCart(this.cartItems);
        }
        this.cartItems = this.localService.getCart();
      });
    }

    this.cartItemsCopy = JSON.parse(JSON.stringify(this.cartItems));
    this.loadCustomerData();
    if (this.cartItems != null) {
      this.totalAmountForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
      this.totalQuantityForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.quantity, 0);
    }
  }

  update() {
    this.localService.setCart(this.cartItems);
    this.cartItems = this.localService.getCart();
    this.totalAmountForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
    this.totalQuantityForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.quantity, 0);
    this.customerDiscountCodes = this.filterDiscountCodes(this.allDiscountCodes);
  }

  clearCart() {
    this.localService.clearCart();
    this.cartItems = this.localService.getCart();
  }

  retrieveDiscountCodesForCustomer(customerId: number) {
    this.discountCodeService.retrieveDiscountCodesForCustomer(customerId).subscribe(response => {
      // console.log(response.discountCodeEntities);
      this.allDiscountCodes = response.discountCodeEntities;
      this.customerDiscountCodes = this.filterDiscountCodes(this.allDiscountCodes);
    });
  }

  loadCustomerData() {
    this.retrieveDiscountCodesForCustomer(this.sessionService.getCurrentCustomer().customerId);
    this.currentCustomer = this.sessionService.getCurrentCustomer();
  }

  getMaxPointsAllowed(): number {
    const remainingPoints: number = this.currentCustomer.remainingPoints;
    if (!this.applyingDiscountForCart && !this.applyingDiscountForItems) {
      return remainingPoints < Math.floor(this.totalAmountForTheCart * 0.5 / 0.05) ? Math.floor(remainingPoints) : Math.floor(this.totalAmountForTheCart * 0.5 / 0.05);
    } else if (this.applyingDiscountForCart) {
      if (this.flatDiscountAmountForCart != null) {
        return remainingPoints < Math.floor((this.totalAmountForTheCart - this.flatDiscountAmountForCart) * 0.5 / 0.05) ? Math.floor(remainingPoints) : Math.floor((this.totalAmountForTheCart - this.flatDiscountAmountForCart) * 0.5 / 0.05);
      } else if (this.rateDiscountAmountForCart != null) {
        return remainingPoints < Math.floor((this.totalAmountForTheCart - this.totalAmountForTheCart * (this.rateDiscountAmountForCart / 100)) * 0.5 / 0.05) ? Math.floor(remainingPoints) : Math.floor((this.totalAmountForTheCart - this.totalAmountForTheCart * (this.rateDiscountAmountForCart / 100)) * 0.5 / 0.05);
      }
    } else if (this.applyingDiscountForItems) {
      return remainingPoints < Math.floor(this.totalAmountDiscounted * 0.5 / 0.05) ? Math.floor(remainingPoints) : Math.floor(this.totalAmountDiscounted * 0.5 / 0.05);
    }
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
    return (new Intl.NumberFormat('en-SG', {style: 'currency', currency: 'SGD'}).format(currency));
  }

  checkout(cartItems: CartItem[]) {

    this.saleTransactionService.createSaleTransaction(cartItems, this.discountCodeToApply, this.pointsToUse).subscribe(response => {

      if (response != 0) {
        if (response === '4') {
          this.errorMessage = 'Error checking out, reloading the page';
          window.location.reload();
        } else {
          this.removeMessageClose = true;
          this.infoMessageClose = false;
          this.infoMessage = 'Thank you for shopping with us!';
          this.localService.clearCart();
          this.cartItems = this.localService.getCart();
          this.updateCustomer(response);
        }
      }
    });
  }

  updateCustomer(response) {
    console.log('Enum: ' + response.saleTransactionEntity.customerEntity.membershipTierEnum);

    const tierInfo = this.customerService.setTierInfo(response.saleTransactionEntity.customerEntity.membershipTierEnum);

    console.log('Test');
    console.log(tierInfo.tierMessage);
    console.log(tierInfo.tierUrl);
    response.saleTransactionEntity.customerEntity.tierMessage = tierInfo.tierMessage;
    response.saleTransactionEntity.customerEntity.tierUrl = tierInfo.tierUrl;

    this.sessionService.setCurrentCustomer(response.saleTransactionEntity.customerEntity);
  }

  add(cartItem: CartItem) {
    cartItem.quantity = cartItem.quantity + 1;
    cartItem.subTotal = cartItem.quantity * cartItem.unitPrice;
    this.update();
  }

  minus(cartItem: CartItem) {
    cartItem.quantity = cartItem.quantity - 1;
    cartItem.subTotal = cartItem.quantity * cartItem.unitPrice;
    this.update();
  }

  resetSelected() {
    this.rateDiscountAmountForCart = null;
    this.flatDiscountAmountForCart = null;
    this.applyingDiscountForCart = false;
    this.applyingDiscountForItems = false;
  }

  applyDiscountCode() {
    this.discountCodeToApply = JSON.parse(JSON.stringify(this.currentSelectedDiscountCode));
    if (this.discountCodeToApply != null) {
      console.log('APPLY 1');
      if (this.discountCodeToApply.productEntities == null || this.discountCodeToApply.productEntities.length == 0) {
        console.log('APPLY 2');
        this.applyDiscountCodeToShoppingCart();
      } else { // for some products
        this.cartItemsCopy = JSON.parse(JSON.stringify(this.cartItems));
        this.applyDiscountCodeToCartItems(this.discountCodeToApply, this.cartItemsCopy);
      }
    }
  }

  applyDiscountCodeToCartItems(discountCode: DiscountCode, cartItems: CartItem[]) {
    const cartItemsToDiscount: CartItem[] = this.getCartItemsToApplyDiscountTo(discountCode, cartItems);

    if (discountCode.discountCodeTypeEnum.toString() == 'FLAT') {
      const discountAmount: number = discountCode.discountAmountOrRate;

      cartItemsToDiscount.forEach(cartItem => {
        if (cartItem.subTotal - discountAmount > 0) {
          cartItem.subTotal = cartItem.subTotal - discountAmount;
        }
      });
    } else if (discountCode.discountCodeTypeEnum.toString() == 'PERCENTAGE') {
      const discountRate: number = discountCode.discountAmountOrRate;

      cartItemsToDiscount.forEach(cartItem => {
        const discountBy: number = cartItem.subTotal * discountRate / 100;

        cartItem.subTotal = cartItem.subTotal - discountBy;
      });
    }
    this.totalAmountDiscounted = this.cartItemsCopy.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
    this.applyingDiscountForItems = true;
  }

  getCartItemsToApplyDiscountTo(discountCode: DiscountCode, cartItems: CartItem[]) {
    const cartItemsToDiscount: CartItem[] = [];
    const discountCodeProducts = discountCode.productEntities;
    // console.log(discountCodeProducts);
    discountCodeProducts.forEach(dcProduct => {
      cartItems.forEach(cartItem => {
        if (dcProduct.productId == cartItem.productEntity.productId) {
          cartItemsToDiscount.push(cartItem);
        }
      });
    });
    // console.log(cartItemsToDiscount);
    return cartItemsToDiscount;
  }


  applyDiscountCodeToShoppingCart() {
    const discountCodeType = this.discountCodeToApply.discountCodeTypeEnum.toString();
    console.log(discountCodeType);
    if (discountCodeType == 'FLAT') {
      this.rateDiscountAmountForCart = null;
      this.flatDiscountAmountForCart = this.discountCodeToApply.discountAmountOrRate;
    } else if (discountCodeType == 'PERCENTAGE') {
      this.flatDiscountAmountForCart = null;
      this.rateDiscountAmountForCart = this.discountCodeToApply.discountAmountOrRate; // eg 5%
    }
    this.applyingDiscountForCart = true;
  }

  // DISCOUNT CODE FILTERING

  getDiscountCodeMessage(discountCodeId: number): string {
    // If discount code is for some products, return a string of which products
    // If discount code is for all products, return a string of "ALL"
    let message = '(';

    const discountCodeToProcess: DiscountCode = this.customerDiscountCodes.find(dc =>
      dc.discountCodeId == discountCodeId);

    const discountCodeAmount = discountCodeToProcess.discountAmountOrRate;
    if (discountCodeToProcess.discountCodeTypeEnum.toString() == 'FLAT') {
      message += this.format(discountCodeAmount) + ' Off '; // e.g. $5
    } else if (discountCodeToProcess.discountCodeTypeEnum.toString() == 'PERCENTAGE') {
      message += discountCodeAmount + '% Off ';
    }

    if (discountCodeToProcess.productEntities == null || discountCodeToProcess.productEntities.length == 0) {
      message += 'Entire Cart)';
      return message;
    }

    const discountCodeProducts: Product[] = discountCodeToProcess.productEntities;

    // for each discount code product, check if product exists in shopping cart
    discountCodeProducts.forEach(dcProduct => {

      if (this.isDiscountCodeProductInCart(dcProduct.productId)) {
        message += dcProduct.name + ', ';
      }


    });

    message = message.slice(0, message.length - 2);
    message += ')';

    return message;
  }

  isDiscountCodeProductInCart(dcProductId: number): boolean {
    const foundCartItem: CartItem = this.cartItems.find(cartItem => cartItem.productEntity.productId == dcProductId);

    if (foundCartItem != null) {
      return true;
    } else {
      return false;
    }
  }

  // applicable if all of its products are in cart, or it is meant for all products
  isDiscountCodeApplicable(discountCode: DiscountCode): boolean {
    if (discountCode.productEntities == null || discountCode.productEntities.length == 0) {
      return true;
    }
    const discountCodeProducts: Product[] = discountCode.productEntities;
    // console.log(discountCodeProducts);
    // for each discount code product, check if product exists in shopping cart
    for (let i = 0; i < discountCodeProducts.length; i++) {
      if (this.isDiscountCodeProductInCart(discountCodeProducts[i].productId)) {
        return true;
      }
    }

    return false;

  }

  filterOutInapplicableDiscountCodes(discountCodes: DiscountCode[]): DiscountCode[] {
    // console.log(discountCodes);
    if (discountCodes != null) {
      const filteredDiscountCodes: DiscountCode[] = discountCodes.filter(discountCode =>
        this.isDiscountCodeApplicable(discountCode)
      );
      // console.log(filteredDiscountCodes);
      return filteredDiscountCodes;
    }

  }

  filterValidDiscountCodes(discountCodes: DiscountCode[]): DiscountCode[] {
    const now: Date = new Date();
    const filteredDiscountCodes = [];
    discountCodes.forEach(discountCode => {
      const endDate: Date = new Date(discountCode.endDate);
      const startDate: Date = new Date(discountCode.startDate);
      if (endDate > now && startDate < now) {
        filteredDiscountCodes.push(discountCode);
      }
    });
    return filteredDiscountCodes;
  }

  filterOutUnavailableDiscountCodes(discountCodes: DiscountCode[]): DiscountCode[] {
    const filteredDiscountCodes = [];
    discountCodes.forEach(discountCode => {
      if (discountCode.numAvailable > 0) {
        filteredDiscountCodes.push(discountCode);
      }
    });
    return filteredDiscountCodes;
  }

  filterOutMoreThanShoppingCart(discountCodes: DiscountCode[]): DiscountCode[] {
    return discountCodes.filter(discountCode => !(discountCode.discountCodeTypeEnum.toString() == 'FLAT' && discountCode.discountAmountOrRate > this.totalAmountForTheCart));
  }

  filterDiscountCodes(discountCodes: DiscountCode[]): DiscountCode[] {
    return this.filterOutMoreThanShoppingCart(this.filterOutUnavailableDiscountCodes(this.filterValidDiscountCodes(this.filterOutInapplicableDiscountCodes(discountCodes))));
  }
}
