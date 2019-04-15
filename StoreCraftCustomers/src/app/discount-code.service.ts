import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import {DiscountCode} from './discount-code'

@Injectable({
  providedIn: 'root'
})
export class DiscountCodeService {

  baseUrl = '/api/DiscountCode';

  constructor(private httpClient: HttpClient) { }

  retrieveDiscountCodesForCustomer(customerId : number): Observable<any>{
    return this.httpClient.get<any>(this.baseUrl + "/retrieveDiscountCodesForCustomer?customerId=" + customerId)
    .pipe(
      catchError(this.handleError)
    )
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
