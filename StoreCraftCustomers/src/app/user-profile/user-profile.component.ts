import { Component, OnInit, SimpleChanges, OnChanges } from '@angular/core';
import { SessionService } from '../session.service';
import { Customer } from '../customer';
import { MembershipTierEnum } from '../MembershipTierEnum.enum';
import { CustomerService } from '../customer.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit, OnChanges {

  customer: Customer;
  customerMemTierString:string;

  isUpdating: boolean;
  infoMessage: string;
  errorMessage: string;

  constructor(
    public sessionService: SessionService,
    public customerService: CustomerService) {
    this.isUpdating = false;
   }

  ngOnInit() {
    this.customer = this.sessionService.getCurrentCustomer();
    this.setTierInfo();
  }

  updating(){
    this.isUpdating = true;
  }

  cancelUpdating(){
    this.isUpdating = false;
  }

  saveChanges(){
    this.customerService.updateCustomer(this.customer).subscribe(
      response => {
        console.log(response);
        this.sessionService.setCurrentCustomer(this.customer);
        this.infoMessage = "Succesfully updated!";
        this.isUpdating = false;
      },
      error => {
        console.log(error);
        this.errorMessage = error;
      }
    );
  }

  setTierInfo() {
    
    console.log("Is this num or enum" + this.customer.membershipTierEnum);
    
    let response = this.customerService.setTierInfo(this.customer.membershipTierEnum);

    this.customer.tierMessage = response.tierMessage;
    this.customer.tierUrl = response.tierUrl;
  }

}
