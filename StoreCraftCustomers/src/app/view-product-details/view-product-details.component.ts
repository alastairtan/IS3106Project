import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Product } from '../product';
import { ProductService } from '../product.service';
import { LocalService } from '../local.service';
import { CartItem } from '../cartItem';

@Component({
  selector: 'app-view-product-details',
  templateUrl: './view-product-details.component.html',
  styleUrls: ['./view-product-details.component.css'],
})
export class ViewProductDetailsComponent implements OnInit {

  private product: Product;
  private quantity: number = 0;
  private errorMessage: string;

  constructor(private activatedRoute: ActivatedRoute,
    private productService: ProductService,
    private localService: LocalService) { }

  ngOnInit() {
    let productId = parseInt(this.activatedRoute.snapshot.paramMap.get('productId'));

    this.productService.getProductId(productId).subscribe(response =>
      this.product = response.productEntity
    ),
      (error: string) => {
        this.errorMessage = error;
      }
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

      if ( existingCartItem != null ) 
      {
        let index = cartItems.indexOf(existingCartItem);
        
        cartItems[index].quantity += newCartItem.quantity;
      }
      else {
        cartItems.push(newCartItem);
      }
    } 
    else 
    {
      cartItems = [];
      cartItems.push(newCartItem);
    }

    if ( newCartItem.quantity != 0 ) 
    {
      this.localService.setCart(cartItems);
    }

    console.log(cartItems);
  }
}
