import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CartItem } from './cartItem';
import { SaleTransaction } from './saleTransaction';
import { SessionService } from './session.service';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class SaleTransactionService {

  baseUrl: string = "/api/SaleTransaction"

  constructor(public httpClient: HttpClient, 
    public sessionService: SessionService) { }


  createSaleTransaction(cartItems: CartItem[]): Observable<any> {

    let totalAmountForTheCart = cartItems.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
    let totalQuantityForTheCart = cartItems.reduce((acc, cartItem) => acc + cartItem.quantity, 0);

    let num = 1;

    cartItems.map((cartItem) => {
      cartItem.serialNumber = num;
      num += 1;
     })

    let saleTransaction: SaleTransaction;
    saleTransaction = new SaleTransaction(cartItems.length, totalQuantityForTheCart, totalAmountForTheCart,
      new Date(), false, this.sessionService.getCurrentCustomer(), cartItems);
    
      console.log(saleTransaction);

      let saleTransactionReq = {"saleTransactionEntity" : saleTransaction};

      return this.httpClient.put<any>(this.baseUrl + "/createSaleTransaction", saleTransactionReq, httpOptions).pipe
      (
        catchError(this.handleError)
      )
  }


  retrieveAllTransactions() : Observable<any> {
    return this.httpClient.get<any>(this.baseUrl).pipe
    (
      catchError(this.handleError)
    );
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
