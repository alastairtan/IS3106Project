import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommunityGoalService {
  baseUrl: string = "/api/CommunityGoal"
  

  constructor(private httpClient: HttpClient) { 
    
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

  retrieveCurrentCommunityGoalsByCountry(country: string): Observable<any> {
		return this.httpClient.get<any>(this.baseUrl + "/retrieveCurrentCommunityGoalsByCountry?country=" +country).pipe
		(
			catchError(this.handleError)
		);
	}
}
