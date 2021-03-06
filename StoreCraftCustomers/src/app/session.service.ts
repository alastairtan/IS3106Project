import { Injectable } from '@angular/core';
import { Customer } from './customer';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  public isLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private httpClient: HttpClient) { }

  getIsLogin(): boolean {
    if (sessionStorage.isLogin == 'true') {
      return true;
    } else {
      return false;
    }
  }

  setIsLogin(isLogin: boolean): void {
    sessionStorage.isLogin = isLogin;
    this.isLoggedIn.next(isLogin);
  }


  getCurrentCustomer(): Customer {
    if (sessionStorage.currentCustomer != null) {
      return JSON.parse(sessionStorage.currentCustomer);
    } else {
      return null;
    }
  }


  refreshCurrentCustomer(): Customer {
    if (sessionStorage.currentCustomer != null) {
      const customer: Customer = JSON.parse(sessionStorage.currentCustomer);
      this.httpClient.get<any>('/api/Customer/getCustomerById?customerId=' + customer.customerId).subscribe(response => {
        if (customer != null) {
          this.setCurrentCustomer(response.customerEntity);
          return response.customerEntity;
        }
      });
    } else {
      return null;
    }
  }




  setCurrentCustomer(currentCustomer: Customer): void {
    sessionStorage.currentCustomer = JSON.stringify(currentCustomer);
  }

  setSelectedCategory(categoryId: number): void {
    sessionStorage.selectedCategory = categoryId;
  }

  getSelectedCategory(): number {
    return sessionStorage.selectedCategory;
  }

}
