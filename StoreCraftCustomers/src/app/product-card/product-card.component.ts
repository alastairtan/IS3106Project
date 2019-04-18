import { Component, OnInit, Input } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';

import { ActivatedRoute, Router } from '@angular/router';
import { DataService } from '../data.service';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})

export class ProductCardComponent implements OnInit {

  @Input()
  product: Product;

  averageRating: number;

  numberOfRatings: number;

  private productData;

  constructor(private router: Router, private data: DataService, private productService: ProductService) { }

  ngOnInit() {
    this.data.currentData.subscribe(data => this.productData = data);
    this.getRatingInfo();
  }

  // viewProduct() {
  //   this.data.updateData(JSON.stringify(this.product));
  //   this.router.navigate(['/product', this.product.productId]);
  // }

  getRatingInfo() {
    this.productService.getRatingInfoForProduct(this.product.productId).subscribe(response => {
      this.averageRating = response.result[0];
      this.numberOfRatings = response.result[1];
    })
  }


  format(currency: number) {
    return (new Intl.NumberFormat('en-SG', { style: 'currency', currency: 'SGD' }).format(currency));
  }

}
