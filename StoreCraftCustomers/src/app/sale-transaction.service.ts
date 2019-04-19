import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CartItem } from './cartItem';
import { SaleTransaction } from './saleTransaction';
import { SessionService } from './session.service';
import { DiscountCode } from './discount-code';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class SaleTransactionService {

  baseUrl = '/api/SaleTransaction';

  constructor(public httpClient: HttpClient,
    public sessionService: SessionService) { }


  createSaleTransaction(cartItems: CartItem[], discountCode: DiscountCode, pointsToUse: number): Observable<any> {

    const totalAmountForTheCart = cartItems.reduce((acc, cartItem) => acc + cartItem.subTotal, 0);
    const totalQuantityForTheCart = cartItems.reduce((acc, cartItem) => acc + cartItem.quantity, 0);
    // discount code discounts are applied on server-side/EJB
    // so are points to use
    let num = 1;

    cartItems.map((cartItem) => {
      cartItem.serialNumber = num;
      num += 1;
     });

    let saleTransaction: SaleTransaction;
    saleTransaction = new SaleTransaction(cartItems.length, totalQuantityForTheCart, totalAmountForTheCart,
      new Date(), false, this.sessionService.getCurrentCustomer(), cartItems, discountCode, pointsToUse);

      console.log(saleTransaction);

      const saleTransactionReq = {'saleTransactionEntity' : saleTransaction};

      return this.httpClient.put<any>(this.baseUrl + '/createSaleTransaction', saleTransactionReq, httpOptions).pipe
      (
        catchError(error => {

          if (error.status == '500') {
            this.handleError(error);
          } else {
            console.log(error);
            return JSON.stringify(error.status);
          }
        })
      );
  }


  retrieveSaleTransactionByCustomerId(): Observable<any> {
    return this.httpClient.get<any>(this.baseUrl + '/retrieveSaleTransactionByCustomerId?customerId='
    + this.sessionService.getCurrentCustomer().customerId).pipe
    (
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
