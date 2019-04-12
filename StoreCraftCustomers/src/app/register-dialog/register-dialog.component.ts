import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';

import { CustomerService } from '../customer.service';
import { Customer } from '../customer';
import { MembershipTierEnum } from '../MembershipTierEnum.enum';

@Component({
  selector: 'app-register-dialog',
  templateUrl: './register-dialog.component.html',
  styleUrls: ['./register-dialog.component.css']
})
export class RegisterDialogComponent implements OnInit {
  
  infoMessage: string;
  errorMessage: string;
  customer : Customer;

  constructor(
    public dialogRef: MatDialogRef<RegisterDialogComponent>,
    public customerService: CustomerService,
    public router: Router,
    private activatedRoute: ActivatedRoute
  ) { 
    this.customer = new Customer();
  }

  ngOnInit() {
    
  }

  exit() : void {
    this.dialogRef.close();
  }

  registerCustomer() {

    this.customer.profilePicUrl = "https://www.superherodb.com/pictures2/portraits/10/050/133.jpg";

    console.log(this.customer.email);
    console.log(this.customer.firstName);
    console.log(this.customer.lastName);
    console.log(this.customer.country);
    
    this.customerService.customerRegister(this.customer).subscribe(
      response=> {
        this.infoMessage = "New customer " + response.customerEntity + " created successfully";
        this.errorMessage = null;
      },
      error => {
        this.infoMessage = null;
        this.errorMessage = error;
      }
    )
  };

}
