import { Component, OnInit } from '@angular/core';
import { ProductService } from '../product.service';
import { Product } from '../product';
import { SessionService } from '../session.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-view-products',
  templateUrl: './view-products.component.html',
  styleUrls: ['./view-products.component.css']
})
export class ViewProductsComponent implements OnInit {

  products: Product[];

  constructor(public productService: ProductService,
    public sessionService: SessionService,
    private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    let selectedCategory: number;
    this.activatedRoute.params.subscribe(params => {
      selectedCategory = parseInt(params["categoryId"])
      this.productService.getProductsByCategory(selectedCategory).subscribe(response => {
        this.products = response.productEntities;
        console.log(this.products);
      })
    });
    //let selectedCategory: number = parseInt(this.activatedRoute.snapshot.paramMap.get("categoryId"))
    /*this.productService.getProductsByCategory(selectedCategory).subscribe(response => {
      this.products = response.productEntities;
      console.log(this.products);
    })*/
  }



}
