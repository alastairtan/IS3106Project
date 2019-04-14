import { Component, OnInit } from '@angular/core';
import { CartItem } from '../cartItem';
import { LocalService } from '../local.service';
import { SaleTransactionService } from '../sale-transaction.service';

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

  constructor(private localService: LocalService, private saleTransactionService: SaleTransactionService) { }

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
  }

  format(currency: number) {
    return (new Intl.NumberFormat('en-SG', { style: 'currency', currency: 'SGD' }).format(currency));
  }

  checkout(cartItems: CartItem[]) {

    this.saleTransactionService.createSaleTransaction(cartItems).subscribe(response => {
      console.log("What is the response" + response);
    }
    ), (error: string) => {
      console.log("Error 500?? " + error);
    }

    this.localService.clearCart();
    this.cartItems = this.localService.getCart();
  }
}
