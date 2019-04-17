import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Customer } from './customer';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  baseUrl: string = "/api/Customer"

  constructor(private httpClient: HttpClient) {
  }

  customerLogin(email: string, password: string): Observable<any> {
    return this.httpClient.get<any>(this.baseUrl + "/customerLogin?email=" +
      email + "&password=" + password).pipe(
        catchError(this.handleError)
      )
  }

  customerRegister(customer: Customer): Observable<any> {

    let customerReq = { "customerEntity": customer };

    return this.httpClient.put<any>(this.baseUrl, customerReq, httpOptions).pipe
      (
        catchError(this.handleError)
      )
  }

  updateCustomer(customer: Customer): Observable<any> {
    customer.password = "";
    let customerReq = { "customerEntity": customer };

    return this.httpClient.put<any>(this.baseUrl + "/updateCustomer", customerReq, httpOptions).pipe
      (
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

  setTierInfo(membershipTierEnum){
    
    let response = {
      tierMessage : "",
      tierUrl : ""
    }

    switch(membershipTierEnum){
      case "BRONZE": {
        response.tierMessage = "Welcome, Bronzie!";
        response.tierUrl = "https://opgg-static.akamaized.net/images/medals/bronze_1.png";
        break;
      }
      case "SILVER" : {
        response.tierMessage = "Every cloud has a Silver lining!";
        response.tierUrl = "https://opgg-static.akamaized.net/images/medals/silver_1.png";
        break;
      }
      case "GOLD" : {
        response.tierMessage = "AU YEAH!!! Gold! ";
        response.tierUrl = "https://opgg-static.akamaized.net/images/medals/gold_1.png";
        break;
      }
      case "PLATINUM" : {
        response.tierMessage = "Platinum means you're halfway there!";
        response.tierUrl = "https://opgg-static.akamaized.net/images/medals/platinum_1.png";
        break;
      }
      case "DIAMOND" : {
        response.tierMessage = "Shine bright like a Diamond!";
        response.tierUrl = "https://opgg-static.akamaized.net/images/medals/diamond_1.png";
        break;
      }
      case "MASTER" : {
        response.tierMessage = "Just a little more to go, Master!";
        response.tierUrl = "https://opgg-static.akamaized.net/images/medals/master_1.png";
        break;
      }
      case "GRANDMASTER" : {
        response.tierMessage = "RAMPAGEEE!! Congratulations, Grandmaster!";
        response.tierUrl = "https://opgg-static.akamaized.net/images/medals/grandmaster_1.png";
        break;
      }
    }

    return response;
  }

  retrieveCustomersBySpendingPerMonth(): Observable<any> {
    console.log("HI from spending per month");
    return this.httpClient.get<any>(this.baseUrl + "/retrieveCustomersBySpendingPerMonth").pipe(
      catchError(this.handleError)
    );
  }

  retrieveCustomersBySpendingTotal(): Observable<any> {
    console.log("HI from spending total");
    return this.httpClient.get<any>(this.baseUrl + "/retrieveCustomersBySpendingTotal").pipe(
      catchError(this.handleError)
    );
  }

}
