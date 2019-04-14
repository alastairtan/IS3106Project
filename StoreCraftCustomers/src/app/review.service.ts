import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Review } from './review';
import { SessionService } from './session.service';

const httpOptions = {
	headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
	providedIn: 'root'
})
export class ReviewService {

	baseUrl: string = "/api/Review";

	constructor(private httpClient: HttpClient,
		private sessionService: SessionService) { }

	getReviewChain(reviewId: number): Observable<any> {
		return this.httpClient.get<any>(this.baseUrl + "/retrieveReviewChain?reviewId=" + reviewId)
			.pipe(catchError(this.handleError))
	}


	replyToStaffReply(custReply: Review, staffReply: Review, customerId: number): Observable<any> {

		let replyToStaffReplyReq = {
			"staffReplyId": staffReply.reviewId,
			"customerReply": custReply,
			"customerId": customerId
		}
		
		return this.httpClient.put<any>(this.baseUrl + '/replyToStaffReply/', replyToStaffReplyReq, httpOptions).pipe(
			catchError(this.handleError)
		)
	}
	
	updateReview(reviewId: number, newContent: string, newProductRating: number): Observable<any>{

		let updateReviewReq = {
			"reviewId": reviewId,
			"newContent": newContent,
			"newProductRating": newProductRating

		}
		return this.httpClient.put<any>(this.baseUrl + '/updateReview', updateReviewReq, httpOptions).pipe(
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
