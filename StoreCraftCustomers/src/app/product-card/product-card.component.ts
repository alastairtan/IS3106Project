import { Component, OnInit, Input } from '@angular/core';
import { Product } from '../product';

import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})
export class ProductCardComponent implements OnInit {

  @Input() 
  product: Product


  constructor(private router:Router) { }

  ngOnInit() {
  }

  viewProduct() {
    this.router.navigate(['/product', this.product.productId]);
  }

}
