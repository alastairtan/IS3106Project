import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Product} from './product';

@Injectable({
  providedIn: 'root'
})

export class ProductService {

  baseUrl = '/api/Product';

  constructor(private httpClient: HttpClient) {

  }

  getAllProducts(): Observable<any> {
    return this.httpClient.get<any>(this.baseUrl + '/retrieveAllProducts').pipe(catchError(this.handleError));
  }

  getProductsByCategory(categoryId: number): Observable<any> {
    return this.httpClient.get<any>(this.baseUrl + '/getProductsByCategory?categoryId=' + categoryId)
      .pipe(catchError(this.handleError));
  }

  getProductId(productId: number): Observable<any> {
    return this.httpClient.get<any>(this.baseUrl + '/getProductById?productId=' + productId)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = '';

    if (error.error instanceof ErrorEvent) {
      errorMessage = 'An unknown error has occurred: ' + error.error.message;
    } else {
      errorMessage = 'A HTTP error has occurred: ' + `HTTP ${error.status}: ${error.error.message}`;
    }

    console.error(errorMessage);

    return throwError(errorMessage);
  }

  getRatingInfoForProduct(productId: number): Observable<any> {
    return this.httpClient.get<any>(this.baseUrl + '/getRatingInfoForProduct?productId=' + productId).pipe(
      catchError(this.handleError)
    );
  }

  filterProductsByTagsOR(products: Product[], filterTagIds: number[]): Product[] {

    const filteredList: Product[] = [];

    for (const product of products) {
      const productTagIds: number[] = this.getTagIdsFromTagEntities(product.tagEntities);
      if (this.productTagIdsContainsOne(productTagIds, filterTagIds)) {
        filteredList.push(product);
      }
    }

    return filteredList;
  }

  filterProductsByTagsAND(products: Product[], filterTagIds: number[]): Product[] {

    const filteredList: Product[] = [];

    for (const product of products) {
      const productTagIds: number[] = this.getTagIdsFromTagEntities(product.tagEntities);
      if (this.productTagIdsContainsAll(productTagIds, filterTagIds)) {
        filteredList.push(product);
      }
    }

    return filteredList;
  }


  getTagIdsFromTagEntities(tags): number[] {
    const tagIds: number[] = [];
    for (const tag of tags) {
      tagIds.push(tag.tagId);
    }
    return tagIds;
  }

  productTagIdsContainsOne(productTagIds: number[], filterTagIds: number[]) {
    for (const filterTagId of filterTagIds) {
      if (productTagIds.includes(filterTagId)) {
        return true;
      }
    }
    return false;
  }

  productTagIdsContainsAll(productTagIds: number[], filterTagIds: number[]) {
    for (const filterTagId of filterTagIds) {
      if (!productTagIds.includes(filterTagId)) {
        return false;
      }
    }
    return true;
  }

  getRandomProductsForIndexPage(): Observable<any> {
    return this.httpClient.get<any>(this.baseUrl + '/index').pipe
    (
      catchError(this.handleError)
    );
  }


}
