import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Review } from './review';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  baseUrl: string = "/api/Review";

  constructor(private httpClient: HttpClient) { }

  getReviewChain(reviewId: number): Observable<any> {
		return this.httpClient.get<any>(this.baseUrl + "/retrieveReviewChain?reviewId=" + reviewId)
			.pipe(catchError(this.handleError))
  }
  
  replyToStaffReply(review : Review): Observable<any> {
	//   return this.httpClient.put<any>(this.baseUrl + `/replyToStaffReply/${review.}` + )
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
