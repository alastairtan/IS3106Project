import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { SessionService } from './session.service';

@Injectable({
  providedIn: 'root'
})

export class ProductService {

  baseUrl: string = "/api/Product";

  constructor(private httpClient: HttpClient) { 

  }

  getProductsByCategory(categoryId: number){
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


}
