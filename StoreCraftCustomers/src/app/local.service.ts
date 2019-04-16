import { Injectable } from '@angular/core';
import { SessionService } from './session.service';
import { CartItem } from './cartItem';

@Injectable({
  providedIn: 'root'
})
export class LocalService {
  
  private key : string;

  constructor(
    public sessionService: SessionService ) { }

  getCart() {

    if ( this.sessionService.getCurrentCustomer() != null ) {
      
      this.key = this.sessionService.getCurrentCustomer().customerId.toString();

      if (localStorage.getItem(this.key) != null) 
      {
        return JSON.parse(localStorage.getItem(this.key));
      }
      else
      {
        return null;
      }
    } else {
      return null;
    }
  }

  setCart(cartItems : CartItem[]) {

    if ( this.sessionService.getCurrentCustomer() != null ) {
      
      this.key = this.sessionService.getCurrentCustomer().customerId.toString();
      
      if ( cartItems.length != 0 ) 
      {
        localStorage.setItem(this.key, JSON.stringify(cartItems));
      }
      else
      {
        localStorage.removeItem(this.key);
      }
    } else {
      console.log("Please log in!");
    }
  }

  clearCart() {
    this.key = this.sessionService.getCurrentCustomer().customerId.toString();
    localStorage.removeItem(this.key);
  }

  getNumCartItems(){
    let cart = this.getCart();
    if (cart != null){
      return cart.length;
    } else {
      return 0;
    }
  }

  getSubTotal(){
    let cartItems: CartItem[] = this.getCart();
    if (cartItems != null && cartItems.length > 0){
      return cartItems.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
    }
  }
}
