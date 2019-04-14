import { Component, OnInit } from '@angular/core';
import { SessionService } from '../session.service';
import { Customer } from '../customer';
import { MembershipTierEnum } from '../MembershipTierEnum.enum';
import { Product } from '../product';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

  customer: Customer;
  products: Product[];


  constructor(public sessionService: SessionService,
              private productService: ProductService) {

  }

  ngOnInit() {
    if(this.sessionService.getIsLogin() == true) {
      console.log(this.sessionService.getCurrentCustomer());
      this.customer = this.sessionService.getCurrentCustomer();
    }
    
    this.productService.getRandomProductsForIndexPage().subscribe(
			response => {
        this.products = response.productEntities;
        console.log('inside index.component.ts!' + this.products.length);
			},
			error => {
				console.log('********** IndexComponent.ts: ' + error);
			}
		);
  }

}
