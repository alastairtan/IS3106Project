import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {SessionService} from './session.service';


@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  baseUrl = '/api/Category';

  constructor(private httpClient: HttpClient,
              private sessionService: SessionService) {
  }

  getCategories(): Observable<any> {
    return this.httpClient.get<any>(this.baseUrl + '/retrieveAllCategories').pipe
    (
      catchError(this.handleError)
    );
  }

  retrieveCategoryByCategoryId(categoryId: number): Observable<any> {
    return this.httpClient.get<any>(this.baseUrl + '/retrieveCategoryByCategoryId?categoryId=' + categoryId).pipe(
      catchError(this.handleError)
    );
  }

  retrieveAllCategoriesList(): Observable<any> {
    return this.httpClient.get<any>(this.baseUrl + '/retrieveAllCategoriesList').pipe(
      catchError(this.handleError)
    );
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

}
