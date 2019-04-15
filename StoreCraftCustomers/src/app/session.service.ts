import { Injectable } from '@angular/core';
import { Customer } from './customer';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  constructor() { }

  getIsLogin(): boolean {
    if (sessionStorage.isLogin == "true") {
      return true;
    }
    else {
      return false;
    }
  }

  setIsLogin(isLogin: boolean): void {
    sessionStorage.isLogin = isLogin;
  }


  getCurrentCustomer(): Customer {
    if (sessionStorage.currentCustomer != null) {
      return JSON.parse(sessionStorage.currentCustomer);
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
