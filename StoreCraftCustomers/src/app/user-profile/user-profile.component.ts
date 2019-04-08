import { Component, OnInit } from '@angular/core';
import { SessionService } from '../session.service';
import { Customer } from '../customer';
import { MembershipTierEnum } from '../MembershipTierEnum.enum';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  customer: Customer;
  customerMemTierString:string;

  isUpdating: boolean;

  bronzeTierUrl: string;

  constructor(
    public sessionService: SessionService
  ) {
    this.isUpdating = false;
    this.bronzeTierUrl = "https://opgg-static.akamaized.net/images/medals/bronze_1.png";
   }

  ngOnInit() {
    this.customer = this.sessionService.getCurrentCustomer();
    this.customerMemTierString = MembershipTierEnum[this.customer.membershipTierEnum];
    console.log(this.customer.profilePicUrl);
    console.log(MembershipTierEnum[this.customer.membershipTierEnum]);
  }

  updating(){
    this.isUpdating = true;
  }

  cancelUpdating(){
    this.isUpdating = false;
  }

}
