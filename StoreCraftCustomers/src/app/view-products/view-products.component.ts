import { Component, OnInit, ViewChild } from '@angular/core';
import { ProductService } from '../product.service';
import { CategoryService } from '../category.service';
import { Product } from '../product';
import { Category } from '../category';
import { SessionService } from '../session.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PageEvent, MatPaginator } from '@angular/material';
import { MatListOption } from '@angular/material';
import { Observable, forkJoin } from 'rxjs';
import { MatOption } from '@angular/material';

@Component({
  selector: 'app-view-products',
  templateUrl: './view-products.component.html',
  styleUrls: ['./view-products.component.css']
})
export class ViewProductsComponent implements OnInit {

  allProducts: Product[];
  filteredProducts: Product[];
  viewProducts: Product[];

  pageLength: number;
  pageSize: number;
  currentPage: number;

  subCategories: Category[];

  sortSelection = "alphAsc";

  constructor(public productService: ProductService,
    public categoryService: CategoryService,
    public sessionService: SessionService,
    private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    let selectedCategory: number;
    this.activatedRoute.params.subscribe(params => {
      selectedCategory = parseInt(params["categoryId"])
      this.productService.getProductsByCategory(selectedCategory).subscribe(response => {
        this.allProducts = response.productEntities;
        this.filteredProducts = this.allProducts;
        this.pageLength = this.allProducts.length;
        this.pageSize = 4;
        this.currentPage = 0;
        this.renderView();
      })
      this.categoryService.retrieveCategoryByCategoryId(selectedCategory).subscribe(response => {
        this.subCategories = response.categoryEntity.subCategoryEntities;
      })
    });
  }

  handlePageEvent(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.renderView();
  }

  renderView() {
    let start: number = this.currentPage * this.pageSize;
    let end: number = this.currentPage * this.pageSize + this.pageSize;
    this.pageLength = this.filteredProducts.length;
    this.viewProducts = this.filteredProducts.slice(start, end);
    //console.log("RENDER");
    //console.log(this.viewProducts);
  }

  handleCategoryChange(options: MatListOption[]) {

    console.log(options.length + "***")

    if (options.length == 0) { //all not selected
      this.filteredProducts = this.allProducts;
      this.renderView();
      return;
    }

    let selectedCategoryIds: number[] = [];
    let observables: Observable<any>[] = [];

    options.map(o => selectedCategoryIds.push(o.value));

    for (let categoryId of selectedCategoryIds) {
      observables.push(this.productService.getProductsByCategory(categoryId));
    }

    let filtered: Product[] = []
    forkJoin(observables).subscribe(dataArray => {
      for (let response of dataArray) {
        filtered = filtered.concat(response.productEntities);
        console.log(filtered);
      }
      this.filteredProducts = filtered;
      //console.log("DONE");
      this.renderView();
    })

  }

  handleSort(option: MatOption) {
    switch (this.sortSelection) {
      case "alphAsc": {
        this.filteredProducts.sort((a : Product,b: Product) => {
          return a.name.localeCompare(b.name);
        })
        this.renderView();
        break;
      }
      case "alphDsc": {
        this.filteredProducts.sort((a : Product,b: Product) => {
          return a.name.localeCompare(b.name);
        })
        this.filteredProducts.reverse();
        this.renderView();
        break;
      }
      case "pAsc": {
        this.filteredProducts.sort((a: Product, b: Product) => {
          return a.unitPrice - b.unitPrice;
        })
        this.renderView();
        break;
      }
      case "pDsc": {
        this.filteredProducts.sort((a: Product, b: Product) => {
          return a.unitPrice - b.unitPrice;
        })
        this.filteredProducts.reverse();
        this.renderView();
        break;
      }
    }

  }





}
