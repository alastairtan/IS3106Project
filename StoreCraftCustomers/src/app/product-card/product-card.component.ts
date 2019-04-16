import { Component, OnInit, Input } from '@angular/core';
import { Product } from '../product';

import { ActivatedRoute, Router } from '@angular/router';
import { DataService } from '../data.service';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})
export class ProductCardComponent implements OnInit {

  @Input() 
  product: Product

  private productData;

  constructor(private router:Router, private data: DataService) { }

  ngOnInit() {
    this.data.currentData.subscribe(data => this.productData = data);
  }

  viewProduct() {
    this.data.updateData(JSON.stringify(this.product));
    this.router.navigate(['/product', this.product.productId]);
  }

  format(currency: number) {
    return (new Intl.NumberFormat('en-SG', { style: 'currency', currency: 'SGD' }).format(currency));
  }

}
