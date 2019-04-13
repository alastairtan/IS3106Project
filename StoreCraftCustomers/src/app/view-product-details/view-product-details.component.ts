import { Component, OnInit, Input } from '@angular/core';

import { Product } from '../product';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-view-product-details',
  templateUrl: './view-product-details.component.html',
  styleUrls: ['./view-product-details.component.css'],
  inputs: ['product']
})
export class ViewProductDetailsComponent implements OnInit {

  public productId : number;
  constructor(private activatedRoute : ActivatedRoute) {}

  ngOnInit() {
    let product = parseInt(this.activatedRoute.snapshot.paramMap.get('productId'));
  }

}
