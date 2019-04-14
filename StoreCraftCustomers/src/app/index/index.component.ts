import { Component, OnInit } from '@angular/core';
import { SessionService } from '../session.service';
import { Customer } from '../customer';
import { MembershipTierEnum } from '../MembershipTierEnum.enum';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { CommunityGoal } from '../community-goal';
import { CommunityGoalService } from '../community-goal.service';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

  customer: Customer;
  products: Product[];
  communityGoals: CommunityGoal[];
  currentDate: Date = new Date();
  country:string = "Singapore";
  selected: number;
  


  constructor(public sessionService: SessionService,
              private productService: ProductService,
              private communityGoalService: CommunityGoalService) {
    this.currentDate = new Date();
    
  }

  ngOnInit() {
    if(this.sessionService.getIsLogin() == true) {
      console.log(this.sessionService.getCurrentCustomer());
      this.customer = this.sessionService.getCurrentCustomer();
      this.country = this.sessionService.getCurrentCustomer().country;
    }
    
    this.productService.getRandomProductsForIndexPage().subscribe(
			response => {
        this.products = response.productEntities;
        console.log('inside index.component.ts!' + this.products.length);
			},
			error => {
				console.log('********** IndexComponent.ts: ' + error);
			}
    );
    
    this.communityGoalService.retrieveCurrentCommunityGoalsByCountry(this.country).subscribe(
      response => {
        this.communityGoals = response.communityGoalEntities;
        if(this.communityGoals.length != 0) {
          this.selected = this.communityGoals[0].currentPoints*100/this.communityGoals[0].targetPoints;
        }
        console.log('inside index.component.ts! communityGoal' + this.communityGoals.length);
      },
      error => {
        console.log('********** IndexComponent.ts: community ' + error);
      }
    );
  }

}
