import { Component, OnInit } from '@angular/core';
import { SessionService } from '../session.service';
import { Customer } from '../customer';
import { MembershipTierEnum } from '../MembershipTierEnum.enum';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { CommunityGoal } from '../community-goal';
import { CommunityGoalService } from '../community-goal.service';
import { ScavengerHunt } from '../scavengerHunt';
import { ScavengerHuntService } from '../scavenger-hunt.service';

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
  goalAmt: number;
  scavengerHunt: ScavengerHunt;
  scavengerHuntWinners: Customer[];
  scavengerHuntCurr: Customer;
  


  constructor(public sessionService: SessionService,
              private productService: ProductService,
              private communityGoalService: CommunityGoalService,
              private scavengerHuntService: ScavengerHuntService) {
    this.currentDate = new Date();
    this.goalAmt = 0;
    
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
          this.goalAmt = this.communityGoals[0].targetPoints;
        }
        console.log('inside index.component.ts! communityGoal' + this.communityGoals.length);
      },
      error => {
        console.log('********** IndexComponent.ts: community ' + error);
      }
    );

    this.scavengerHuntService.retrieveScavengerHuntForTheDay().subscribe(
      response => {
        this.scavengerHunt = response.scavengerHuntEntity;
        if(this.scavengerHunt != null ) {
          if(this.scavengerHunt.customerEntities.length != 0) {
            this.scavengerHuntWinners = this.scavengerHunt.customerEntities;
          }
          console.log('inside index.component.ts! scavengerHunt!' 
          + this.scavengerHunt.customerEntities.length);
        }
      },
      error => {
        console.log('********** IndexComponent.ts: scavenger ' + error);
      }
      
    )
  }

}
