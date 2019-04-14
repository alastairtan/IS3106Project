import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { SessionService } from './session.service';
import { Product } from './product';
import { Tag } from './tag';

@Injectable({
  providedIn: 'root'
})

export class ProductService {

  baseUrl: string = "/api/Product";

  constructor(private httpClient: HttpClient) { 

  }

  getProductsByCategory(categoryId: number): Observable<any>{
    return this.httpClient.get<any>(this.baseUrl + "/getProductsByCategory?categoryId=" + categoryId)
    .pipe(catchError(this.handleError))
  }

  private handleError(error: HttpErrorResponse) {
		let errorMessage: string = "";

		if (error.error instanceof ErrorEvent) {
			errorMessage = "An unknown error has occurred: " + error.error.message;
		}
		else {
			errorMessage = "A HTTP error has occurred: " + `HTTP ${error.status}: ${error.error.message}`;
		}

		console.error(errorMessage);

		return throwError(errorMessage);
	}

	filterProductsByTagsOR(products: Product[], filterTagIds: number[]): Product[]{

		let filteredList: Product[] = [];

		for (let product of products){
			let productTagIds: number[] = this.getTagIdsFromTagEntities(product.tagEntities);
			if (this.productTagIdsContainsOne(productTagIds, filterTagIds)){
				filteredList.push(product);
			}
		}

		return filteredList;
	}

	filterProductsByTagsAND(products: Product[], filterTagIds: number[]): Product[]{

		let filteredList: Product[] = [];

		for (let product of products){
			let productTagIds: number[] = this.getTagIdsFromTagEntities(product.tagEntities);
			if (this.productTagIdsContainsAll(productTagIds, filterTagIds)){
				filteredList.push(product);
			}
		}

		return filteredList;
	}
	

	getTagIdsFromTagEntities(tags) : number[]{
		let tagIds: number[] = [];
		for (let tag of tags){
			tagIds.push(tag.tagId);
		}
		return tagIds;
	}

	productTagIdsContainsOne(productTagIds: number[], filterTagIds: number[]){
		for (let filterTagId of filterTagIds){
			if (productTagIds.includes(filterTagId)){
				return true;
			}
		}
		return false;
	}

	productTagIdsContainsAll(productTagIds: number[], filterTagIds: number[]){
		for(let filterTagId of filterTagIds){
			if (!productTagIds.includes(filterTagId)){
				return false;
			}
		}
		return true;
	}

	getRandomProductsForIndexPage(): Observable<any> {
		return this.httpClient.get<any>(this.baseUrl + "/index").pipe
		(
			catchError(this.handleError)
		);
	}


}
