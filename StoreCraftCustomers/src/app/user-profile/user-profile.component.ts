import { Component, OnInit } from '@angular/core';
import { SessionService } from '../session.service';
import { Customer } from '../customer';
import { MembershipTierEnum } from '../MembershipTierEnum.enum';
import { CustomerService } from '../customer.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  customer: Customer;
  customerMemTierString:string;

  isUpdating: boolean;
  infoMessage: string;
  errorMessage: string;

  tierUrl: string;
  tierMessage: string;

  constructor(
    public sessionService: SessionService,
    public customerService: CustomerService) {
    this.isUpdating = false;
   }

  ngOnInit() {
    this.customer = this.sessionService.getCurrentCustomer();
    this.customerMemTierString = MembershipTierEnum[this.customer.membershipTierEnum];
    this.setTierInfo();
    console.log(this.customer.profilePicUrl);
    console.log(MembershipTierEnum[this.customer.membershipTierEnum]);
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

  setTierInfo(){
    switch(this.customer.membershipTierEnum){
      case 0: {
        this.tierMessage = "Welcome, Bronzie!";
        this.tierUrl = "https://opgg-static.akamaized.net/images/medals/bronze_1.png";
        break;
      }
      case 1: {
        this.tierMessage = "Every cloud has a Silver lining!";
        this.tierUrl = "https://opgg-static.akamaized.net/images/medals/silver_1.png";
        break;
      }
      case 2: {
        this.tierMessage = "AU YEAH!!! Gold! ";
        this.tierUrl = "https://opgg-static.akamaized.net/images/medals/gold_1.png";
        break;
      }
      case 3: {
        this.tierMessage = "Platinum means you're halfway there!";
        this.tierUrl = "https://opgg-static.akamaized.net/images/medals/platinum_1.png";
        break;
      }
      case 4: {
        this.tierMessage = "Shine bright like a Diamond!";
        this.tierUrl = "https://opgg-static.akamaized.net/images/medals/diamond_1.png";
        break;
      }
      case 5: {
        this.tierMessage = "Just a little more to go, Master!";
        this.tierUrl = "https://opgg-static.akamaized.net/images/medals/master_1.png";
        break;
      }
      case 6: {
        this.tierMessage = "RAMPAGEEE!! Congratulations, Grandmaster!";
        this.tierUrl = "https://opgg-static.akamaized.net/images/medals/grandmaster_1.png";
        break;
      }
    }
  }

}
