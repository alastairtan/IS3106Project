import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from '@angular/material';
import {ActivatedRoute, Router} from '@angular/router';

import {CustomerService} from '../customer.service';
import {SessionService} from '../session.service';
import {Customer} from '../customer';


@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.css']
})
export class LoginDialogComponent implements OnInit {

  username: string;
  password: string;

  loginError: boolean;
  errorMessage: string;

  constructor(
    public dialogRef: MatDialogRef<LoginDialogComponent>,
    public sessionService: SessionService,
    public customerService: CustomerService,
    public router: Router,
    private activatedRoute: ActivatedRoute
  ) {
    this.loginError = false;
  }

  ngOnInit() {
  }

  exit(): void {
    this.dialogRef.close();
  }

  login(): void {

    this.customerService.customerLogin(this.username, this.password).subscribe(
      response => {
        let customer: Customer = response.customerEntity;

        // if (response.customerEntity.membershipTierEnum == 'BRONZE') {
        //   customer.membershipTierEnum = MembershipTierEnum.BRONZE;
        // }
        // if (response.customerEntity.membershipTierEnum == 'SILVER') {
        //   customer.membershipTierEnum = MembershipTierEnum.SILVER;
        // }
        // if (response.customerEntity.membershipTierEnum == 'GOLD') {
        //   customer.membershipTierEnum = MembershipTierEnum.GOLD;
        // }
        // if (response.customerEntity.membershipTierEnum == 'PLATINUM') {
        //   customer.membershipTierEnum = MembershipTierEnum.PLATINUM;
        // }
        // if (response.customerEntity.membershipTierEnum == 'DIAMOND') {
        //   customer.membershipTierEnum = MembershipTierEnum.DIAMOND;
        // }
        // if (response.customerEntity.membershipTierEnum == 'MASTER') {
        //   customer.membershipTierEnum = MembershipTierEnum.MASTER;
        // }
        // if (response.customerEntity.membershipTierEnum == 'GRANDMASTER') {
        //   customer.membershipTierEnum = MembershipTierEnum.GRANDMASTER;
        // }

        if (customer != null) {
          this.sessionService.setCurrentCustomer(customer);

          console.log(this.sessionService.getCurrentCustomer());

          this.loginError = false;

          // this.childEvent.emit();

          this.dialogRef.close();
          this.sessionService.setIsLogin(true);
          // this.router.navigate(this.activatedRoute.snapshot.url);
        }
      }, error => {
        this.loginError = true;
        this.errorMessage = 'Invalid username and/or password. Please try again!';
      });
  }

}
