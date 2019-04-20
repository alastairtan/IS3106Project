import {Component, OnInit} from '@angular/core';
import {SessionService} from '../session.service';
import {Customer} from '../customer';
import {Product} from '../product';
import {ProductService} from '../product.service';
import {CommunityGoal} from '../community-goal';
import {CommunityGoalService} from '../community-goal.service';
import {ScavengerHunt} from '../scavengerHunt';
import {ScavengerHuntService} from '../scavenger-hunt.service';
import {CustomerService} from '../customer.service';

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
  country = '';
  selected: CommunityGoal;
  goalAmt: number;
  currentAmt: number;
  percentage: number;
  scavengerHunt: ScavengerHunt;
  scavengerHuntWinners: Customer[];
  scavengerHuntCurr: Customer;

  customersLeaderboardPerMonth: Customer[];
  customersLeaderboardTotal: Customer[];
  customersLeaderboardPerMonthMax5: Customer[];
  customersLeaderboardTotalMax5: Customer[];
  perMonthNotEmpty: boolean = false;
  totalNotEmpty: boolean = false;


  constructor(public sessionService: SessionService,
              private productService: ProductService,
              private communityGoalService: CommunityGoalService,
              private scavengerHuntService: ScavengerHuntService,
              private customerService: CustomerService) {
    this.currentDate = new Date();
    this.goalAmt = 0;
    this.sessionService.isLoggedIn.subscribe(value => {
      this.refresh();
    });
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    if (this.sessionService.getIsLogin() === true) {
      console.log(this.sessionService.getCurrentCustomer());
      this.customer = this.sessionService.getCurrentCustomer();
      this.country = this.sessionService.getCurrentCustomer().country;
    }
    this.loadProducts();
    this.loadCommunityGoals();
    this.loadScavengerHunt();
  }

  loadProducts() {
    this.productService.getRandomProductsForIndexPage().subscribe(
      response => {
        this.products = response.productEntities.slice(0, 8);
        console.log('inside index.component.ts!' + this.products.length);
      },
      error => {
        console.log('********** IndexComponent.ts: ' + error);
      }
    );
  }

  loadCommunityGoals() {
    this.communityGoalService.retrieveCurrentCommunityGoalsByCountry(this.country).subscribe(
      response => {
        this.communityGoals = response.communityGoalEntities;
        if (this.communityGoals.length != 0) {
          this.selected = this.communityGoals[0];
          this.goalAmt = this.communityGoals[0].targetPoints;
          this.currentAmt = this.communityGoals[0].currentPoints;
          this.percentage = this.currentAmt * 100 / this.goalAmt;
        }
        console.log('inside index.component.ts! communityGoal' + this.communityGoals.length);
      },
      error => {
        console.log('********** IndexComponent.ts: community ' + error);
      }
    );
  }

  loadScavengerHunt() {
    this.scavengerHuntService.retrieveScavengerHuntForTheDay().subscribe(
      response => {
        this.scavengerHunt = response.scavengerHuntEntity;
        if (this.scavengerHunt != null) {
          if (this.scavengerHunt.customerEntities.length != 0) {
            this.scavengerHuntWinners = this.scavengerHunt.customerEntities;
          }
          console.log('inside index.component.ts! scavengerHunt!'
            + this.scavengerHunt.customerEntities.length);
        }
      },
      error => {
        console.log('********** IndexComponent.ts: scavenger ' + error);
      }
    );
  }


  filterCustomerByCountry(customers: Customer[], customersNew: Customer[]) {
    customersNew = [];
    for (var cust of customers) {
      if (customersNew.length <= 5) {
        if (cust.country == this.sessionService.getCurrentCustomer().country) {
          customersNew.push(cust);
        }
      } else {
        break;
      }
    }
    return customersNew;
  }

  onClickChange() {
    this.goalAmt = this.selected.targetPoints;
    this.currentAmt = this.selected.currentPoints;
    this.percentage = this.currentAmt * 100 / this.goalAmt;
  }

}
