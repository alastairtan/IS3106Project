import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { HeaderComponent } from '../header/header.component';
import { Observable, of } from 'rxjs';

import { CustomerService } from '../customer.service';
import { SessionService } from '../session.service';
import { Customer } from '../customer';
import { MembershipTierEnum } from '../MembershipTierEnum.enum';


@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.css']
})
export class LoginDialogComponent implements OnInit {

  username: string;
  password: string;

  constructor(
    public dialogRef: MatDialogRef<LoginDialogComponent>,
    public sessionService: SessionService,
    public customerService: CustomerService,
  ) { }

  ngOnInit() {
  }

  exit(): void {
    this.dialogRef.close();
  }

  login(): void {

    this.customerService.customerLogin(this.username, this.password).subscribe(
      response => {
        let customer: Customer = response.customerEntity;

        if (response.customerEntity.membershipTierEnum == 'BRONZE') {
          customer.membershipTierEnum = MembershipTierEnum.BRONZE;
        }
        if (response.customerEntity.membershipTierEnum == 'SILVER') {
          customer.membershipTierEnum = MembershipTierEnum.SILVER;
        }
        if (response.customerEntity.membershipTierEnum == 'GOLD') {
          customer.membershipTierEnum = MembershipTierEnum.GOLD;
        }
        if (response.customerEntity.membershipTierEnum == 'PLATINUM') {
          customer.membershipTierEnum = MembershipTierEnum.PLATINUM;
        }
        if (response.customerEntity.membershipTierEnum == 'DIAMOND') {
          customer.membershipTierEnum = MembershipTierEnum.DIAMOND;
        }
        if (response.customerEntity.membershipTierEnum == 'MASTER') {
          customer.membershipTierEnum = MembershipTierEnum.MASTER;
        }
        if (response.customerEntity.membershipTierEnum == 'GRANDMASTER') {
          customer.membershipTierEnum = MembershipTierEnum.GRANDMASTER;
        }

        if (customer != null) {
          this.sessionService.setIsLogin(true);
          this.sessionService.setCurrentCustomer(customer);

          console.log("Customer " + JSON.parse(sessionStorage.currentCustomer).firstName);

          //this.loginError = false;

          //this.childEvent.emit();

          //this.router.navigate(["/index"]);
        }
        else {
          //this.loginError = true;
        }
      },
      error => {
        //this.loginError = true;
        //this.errorMessage = error
      })
  }

}
