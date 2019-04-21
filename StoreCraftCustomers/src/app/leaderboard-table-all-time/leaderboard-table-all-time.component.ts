import {Component, OnInit} from '@angular/core';
import {Customer} from '../customer';
import {CustomerService} from '../customer.service';
import {SessionService} from '../session.service';

@Component({
  selector: 'app-leaderboard-table-all-time',
  templateUrl: './leaderboard-table-all-time.component.html',
  styleUrls: ['./leaderboard-table-all-time.component.css']
})
export class LeaderboardTableAllTimeComponent implements OnInit {

  customersLeaderboardTotal: Customer[];
  customersLeaderboardTotalMax5: Customer[];
  perMonthNotEmpty: boolean = false;
  forCountry = '';

  errorMessage: string;
  currentDate: Date = new Date();

  columnsToDisplay = ['First Name', 'Points for Current Month', 'Multiplier'];

  constructor(public customerService: CustomerService,
              public sessionService: SessionService) {
    this.sessionService.isLoggedIn.subscribe(value =>{
      this.refresh();
    })
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.customerService.retrieveCustomersBySpendingTotal().subscribe(
      response => {
        this.customersLeaderboardTotal = response.customerEntities;
        if (this.customersLeaderboardTotal.length != 0) {
          this.perMonthNotEmpty = true;
          this.customersLeaderboardTotalMax5 = this.filterCustomerByCountry(this.customersLeaderboardTotal,
            this.customersLeaderboardTotalMax5);
        }

        this.customersLeaderboardTotalMax5 = this.customersLeaderboardTotalMax5.filter(customer => customer.totalPoints > 0)
        console.log('inside index.component.ts! customerSpendingTotal Max 5: '
          + this.customersLeaderboardTotalMax5.length);
      },
      error => {
        console.log('********** IndexComponent.ts: customerTotal Max 5 ' + error);
      }
    );
  }


  filterCustomerByCountry(customers: Customer[], customersNew: Customer[]) {
    customersNew = [];
    for (var cust of customers) {
      if (customersNew.length <= 5) {
        if (this.sessionService.getCurrentCustomer() == null) {
          // if(cust.country == "Singapore") {
          customersNew.push(cust);
          // }
        } else if (cust.country == this.sessionService.getCurrentCustomer().country) {
          customersNew.push(cust);
          this.forCountry = this.sessionService.getCurrentCustomer().country;
        }
      } else {
        break;
      }
    }
    return customersNew;
  }

  getCountry(): string {
    if (this.forCountry.length === 0  || this.sessionService.getIsLogin() === false) {
      return '(Global)';
    } else {
      return `(${this.forCountry})`;
    }
  }

}

