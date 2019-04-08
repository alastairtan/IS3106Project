import { Component, OnInit } from '@angular/core';

import { ProductService } from '../product.service';
import { CategoryService } from '../category.service';
import { SessionService } from '../session.service';
import { TagService } from '../tag.service';

import { Product } from '../product';
import { Category } from '../category';
import { Tag } from '../tag';

import { ActivatedRoute } from '@angular/router';
import { PageEvent } from '@angular/material';
import { MatListOption } from '@angular/material';
import { Observable, forkJoin } from 'rxjs';
import { SelectionModel } from '@angular/cdk/collections';


@Component({
  selector: 'app-view-products',
  templateUrl: './view-products.component.html',
  styleUrls: ['./view-products.component.css']
})
export class ViewProductsComponent implements OnInit {

  allProducts: Product[];
  filteredProducts: Product[];
  viewProducts: Product[];

  //For Paginator
  pageLength: number;
  pageSize: number;
  currentPage: number;
  //*************

  //For Category Selector
  subCategories: Category[];
  selectedCategoryOptions: number[];
  //*********************

  //For Sort Selector
  sortSelection: string;
  //*****************

  //For Price Range Selector
  minPrice: number;
  maxPrice: number;
  //*****************

  //For Tags Selector
  allTags: Tag[];
  selectedTagOptions: number[];
  condition: string;
  
  constructor(public productService: ProductService,
    public categoryService: CategoryService,
    public sessionService: SessionService,
    public tagService: TagService,
    private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {
    let selectedCategory: number;
    this.activatedRoute.params.subscribe(params => {
      selectedCategory = parseInt(params["categoryId"])
      this.productService.getProductsByCategory(selectedCategory).subscribe(response => {
        this.allProducts = response.productEntities;
        this.filteredProducts = this.allProducts;

        //paginator 
        this.pageLength = this.allProducts.length;
        this.pageSize = 8;
        this.currentPage = 0;
        //********

        //Sorting info
        this.sortSelection = "alphAsc";
        this.minPrice = 0;
        this.maxPrice = 1000;
        //************

        this.handleSort();
      })
      this.categoryService.retrieveCategoryByCategoryId(selectedCategory).subscribe(response => {
        this.subCategories = response.categoryEntity.subCategoryEntities;
      })
      this.tagService.retrieveAllTags().subscribe(response => {
        this.allTags = response.tagEntities;
      })
    });
  }

  handlePageEvent(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.handleCategoryChange(); //cannot use renderView here, otherwise when paginator changes, the price range filter is undone since it doesnt change fiteredProducts permanently
  }

  renderView() {
    this.pageLength = this.filteredProducts.length;
    if (this.pageLength <= this.pageSize) {
      this.currentPage = 0;
    }
    let start: number = this.currentPage * this.pageSize;
    let end: number = this.currentPage * this.pageSize + this.pageSize;
    this.viewProducts = this.filteredProducts.slice(start, end);
    
    //console.log("RENDER");
    //console.log(this.viewProducts);
  }

  handleCategoryChange() {

    if (this.selectedCategoryOptions == null || this.selectedCategoryOptions.length == 0) { //all not selected
      this.filteredProducts = this.allProducts;
      this.handleTagChange();
      return;
    }

    let observables: Observable<any>[] = [];

    for (let categoryId of this.selectedCategoryOptions) {
      observables.push(this.productService.getProductsByCategory(categoryId));
    }

    let filtered: Product[] = []
    forkJoin(observables).subscribe(dataArray => {
      for (let response of dataArray) {
        filtered = filtered.concat(response.productEntities);
        //console.log(filtered);
      }
      this.filteredProducts = filtered;
      //console.log("DONE");
      this.handleTagChange();
    })

  }

  handleSort() {
    switch (this.sortSelection) {
      case "alphAsc": {
        this.filteredProducts.sort((a: Product, b: Product) => {
          return a.name.localeCompare(b.name);
        })
        this.handlePriceRange();
        break;
      }
      case "alphDsc": {
        this.filteredProducts.sort((a: Product, b: Product) => {
          return a.name.localeCompare(b.name);
        })
        this.filteredProducts.reverse();
        this.handlePriceRange();
        break;
      }
      case "pAsc": {
        this.filteredProducts.sort((a: Product, b: Product) => {
          return a.unitPrice - b.unitPrice;
        })
        this.handlePriceRange();
        break;
      }
      case "pDsc": {
        this.filteredProducts.sort((a: Product, b: Product) => {
          return a.unitPrice - b.unitPrice;
        })
        this.filteredProducts.reverse();
        this.handlePriceRange();
        break;
      }
    }
  }

  handlePriceRange() {
    let filteredProductsCopy = Array.from(this.filteredProducts);
    this.filteredProducts = filteredProductsCopy.filter(product => {
      return (product.unitPrice >= this.minPrice && product.unitPrice <= this.maxPrice)
    })
    //console.log(this.filteredProducts);
    this.renderView();
    this.filteredProducts = filteredProductsCopy; //so that original filtered products wont be permanently changed
  }

  handleTagChange(){
    if (this.selectedTagOptions == null || this.selectedTagOptions.length == 0) { //all not selected
      this.filteredProducts = this.allProducts;
      this.handleSort();
      return;
    }

    if (this.condition == null || this.condition == "OR"){
      this.filteredProducts = this.productService.filterProductsByTagsOR(this.filteredProducts, this.selectedTagOptions);
      this.handleSort();
    } else if (this.condition == "AND"){
      this.filteredProducts = this.productService.filterProductsByTagsAND(this.filteredProducts, this.selectedTagOptions);
      this.handleSort();
    }
  }

  doFilter(){
    this.handleCategoryChange();
    this.handleTagChange();
    this.handleSort();
    this.handlePriceRange();
    this.renderView();
  }





}
