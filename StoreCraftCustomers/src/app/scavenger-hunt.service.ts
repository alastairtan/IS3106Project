import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { throwError, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ScavengerHuntService {

  baseUrl: string = "/api/ScavengerHunt"

  constructor(private httpClient: HttpClient) {
  }


  retrieveScavengerHuntForTheDay(): Observable<any> {
    console.log("HI from scavenger hunt");
    return this.httpClient.get<any>(this.baseUrl + "/retrieveScavengerHuntForTheDay").pipe
    (
			catchError(this.handleError)
		);
  }
  
  checkIfCustomerHasWonToday(customerId : number): Observable<any> {

    return this.httpClient.get<any>(this.baseUrl + "/hasCustomerWonToday?customerId=" + customerId)
    .pipe(
      catchError(this.handleError)
    )
  }

  claimScavengerHuntPrize(customerId : number): Observable<any> {

    return this.httpClient.get<any>(this.baseUrl + "/updateWinnerForScavengerHunt/?customerId=" + customerId)
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
