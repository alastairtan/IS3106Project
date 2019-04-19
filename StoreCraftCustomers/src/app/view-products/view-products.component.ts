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
import { Observable, forkJoin } from 'rxjs';


@Component({
  selector: 'app-view-products',
  templateUrl: './view-products.component.html',
  styleUrls: ['./view-products.component.css']
})
export class ViewProductsComponent implements OnInit {

  allProducts: Product[];
  filteredProducts: Product[];
  viewProducts: Product[];

  // For Paginator
  pageLength: number;
  pageSize: number;
  currentPage: number;
  // *************

  // For Category Selector
  subCategories: Category[];
  selectedCategoryOptions: number[];
  // *********************

  // For Sort Selector
  sortSelection: string;
  // *****************

  // For Price Range Selector
  minPrice: number;
  maxPrice: number;
  // *****************

  // For Tags Selector
  allTags: Tag[];
  selectedTagOptions: number[];
  condition: string;

  constructor(public productService: ProductService,
    public categoryService: CategoryService,
    public sessionService: SessionService,
    public tagService: TagService,
    private activatedRoute: ActivatedRoute) {
    this.condition = 'OR';
  }

  ngOnInit() {
    let selectedCategory: number;
    this.activatedRoute.params.subscribe(params => {
      selectedCategory = parseInt(params['categoryId']);
      this.productService.getProductsByCategory(selectedCategory).subscribe(response => {
        console.log(response.productEntities);
        this.allProducts = response.productEntities;
        this.filteredProducts = this.allProducts;


        // paginator
        this.pageLength = this.allProducts.length;
        this.pageSize = 8;
        this.currentPage = 0;
        // ********

        // Sorting info
        this.sortSelection = 'alphAsc';
        this.minPrice = 0;
        this.maxPrice = 10000;
        // ************

        this.doFilter();
      });
      this.categoryService.retrieveCategoryByCategoryId(selectedCategory).subscribe(response => {
        this.subCategories = response.categoryEntity.subCategoryEntities;
      });
      this.tagService.retrieveAllTags().subscribe(response => {
        this.allTags = response.tagEntities;
      });
    });
  }

  handlePageEvent(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.doFilter();
  }

  renderView() {
    this.pageLength = this.filteredProducts.length;
    if (this.pageLength <= this.pageSize) {
      this.currentPage = 0;
    }
    const start: number = this.currentPage * this.pageSize;
    const end: number = this.currentPage * this.pageSize + this.pageSize;
    this.viewProducts = this.filteredProducts.slice(start, end);
    console.log(this.viewProducts);
    // console.log("RENDER");
  }

  handleCategoryChange() {

    return new Promise((resolve, reject) => {

      if (this.selectedCategoryOptions == null || this.selectedCategoryOptions.length == 0) { // all not selected
        this.filteredProducts = this.allProducts;
       // console.log("CATEGORY")
        resolve();
      }

      const observables: Observable<any>[] = [];

      for (const categoryId of this.selectedCategoryOptions) {
        observables.push(this.productService.getProductsByCategory(categoryId));
      }

      let filtered: Product[] = [];
      forkJoin(observables).subscribe(dataArray => {
        for (const response of dataArray) {
          filtered = filtered.concat(response.productEntities);
        }
        this.filteredProducts = filtered;
        // console.log("CATEGORY");
        // console.log(this.filteredProducts)
        resolve();
      });

    });
  }

  handleSort() {
    switch (this.sortSelection) {
      case 'alphAsc': {
        this.filteredProducts.sort((a: Product, b: Product) => {
          return a.name.localeCompare(b.name);
        });
        break;
      }
      case 'alphDsc': {
        this.filteredProducts.sort((a: Product, b: Product) => {
          return a.name.localeCompare(b.name);
        });
        this.filteredProducts.reverse();
        break;
      }
      case 'pAsc': {
        this.filteredProducts.sort((a: Product, b: Product) => {
          return a.unitPrice - b.unitPrice;
        });
        break;
      }
      case 'pDsc': {
        this.filteredProducts.sort((a: Product, b: Product) => {
          return a.unitPrice - b.unitPrice;
        });
        this.filteredProducts.reverse();
        break;
      }
    }
    // console.log("SORT");
  }

  handlePriceRange() {
    const filteredProductsCopy = Array.from(this.filteredProducts);
    this.filteredProducts = filteredProductsCopy.filter(product => {
      return (product.unitPrice >= this.minPrice && product.unitPrice <= this.maxPrice);
    });
    // console.log("PRICE RANGE");
    // console.log(this.filteredProducts);
  }

  handleTagChange() {
    // console.log("TAG CHANGE");
    if (this.selectedTagOptions == null || this.selectedTagOptions.length == 0) { // all not selected
      return;
    }

    if (this.condition == null || this.condition == 'OR') {
      this.filteredProducts = this.productService.filterProductsByTagsOR(this.filteredProducts, this.selectedTagOptions);
    } else if (this.condition == 'AND') {
      this.filteredProducts = this.productService.filterProductsByTagsAND(this.filteredProducts, this.selectedTagOptions);
    }
    console.log(this.filteredProducts);
  }

  doFilter() {
    this.handleCategoryChange().then(() => {
    this.handleTagChange();
    this.handleSort();
    this.handlePriceRange();
    this.renderView();
    });
  }





}
