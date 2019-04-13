import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';

import { CustomerService } from '../customer.service';
import { Customer } from '../customer';
import { MembershipTierEnum } from '../MembershipTierEnum.enum';

import { CountryService } from '../country.service';

@Component({
  selector: 'app-register-dialog',
  templateUrl: './register-dialog.component.html',
  styleUrls: ['./register-dialog.component.css']
})
export class RegisterDialogComponent implements OnInit {
  
  infoMessage: string;
  errorMessage: string;
  customer : Customer;

  countries;

  submitted: boolean;
  

  constructor(
    public dialogRef: MatDialogRef<RegisterDialogComponent>,
    public customerService: CustomerService,
    public router: Router,
    private activatedRoute: ActivatedRoute,
    private countryService: CountryService
  ) { 
    this.customer = new Customer();
    this.submitted = false;
  }

  ngOnInit() {
    this.countryService.getCountries().subscribe(data => {
      this.countries = data;
      console.log(this.countries);
    })
    
  }

  exit() : void {
    this.dialogRef.close();
  }

  registerCustomer() {

    console.log(this.customer.email);
    console.log(this.customer.firstName);
    console.log(this.customer.lastName);
    console.log(this.customer.country);
    
    this.customerService.customerRegister(this.customer).subscribe(
      response=> {
        this.infoMessage = "Welcome, " + response.customerEntity.firstName + "!";
        this.errorMessage = null;
        this.submitted = true;
        this.customer = new Customer();
      },
      error => {
        this.infoMessage = null;
        this.errorMessage = error;
      }
    )
  };

}
