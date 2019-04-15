import { Component, OnInit } from '@angular/core';
import { CartItem } from '../cartItem';
import { LocalService } from '../local.service';
import { SaleTransactionService } from '../sale-transaction.service';
import { SessionService } from '../session.service';
import { UserProfileComponent } from '../user-profile/user-profile.component';
import { CustomerService } from '../customer.service';
import { refreshDescendantViews } from '@angular/core/src/render3/instructions';

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
  private promotionalCode: string;
  private infoMessage : string;
  private errorMessage : string;
  private removeMessage : string;
  private removeMessageClose : boolean;
  private infoMessageClose : boolean;

  constructor(private localService: LocalService, 
    private saleTransactionService: SaleTransactionService,
    private sessionService : SessionService,
    private customerService : CustomerService) { }

  ngOnInit() {
    this.cartItems = this.localService.getCart();
    // this.cartItems.filter() --> to remove item that is no longer available (e.g. deleted)
    if (this.cartItems != null) {
      this.totalAmountForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
      this.totalQuantityForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.quantity, 0);
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

  add(cartItem: CartItem){
    cartItem.quantity = cartItem.quantity + 1;
    cartItem.subTotal = cartItem.quantity * cartItem.unitPrice;
    this.refresh();
  }

  minus(cartItem: CartItem){
    cartItem.quantity = cartItem.quantity - 1;
    cartItem.subTotal = cartItem.quantity * cartItem.unitPrice;
    this.refresh();
  }

  refresh(){
    this.localService.setCart(this.cartItems);
    this.cartItems = this.localService.getCart();
    this.totalAmountForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
    this.totalQuantityForTheCart = this.cartItems.reduce((acc, cartItem) => acc + cartItem.quantity, 0);
  }
  
}
