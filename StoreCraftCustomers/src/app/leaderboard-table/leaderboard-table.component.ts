import {Component, OnInit} from '@angular/core';
import {CustomerService} from '../customer.service';
import {SessionService} from '../session.service';
import {Customer} from '../customer';

@Component({
  selector: 'app-leaderboard-table',
  templateUrl: './leaderboard-table.component.html',
  styleUrls: ['./leaderboard-table.component.css']
})
export class LeaderboardTableComponent implements OnInit {

  customersLeaderboardPerMonth: Customer[];
  customersLeaderboardPerMonthMax5: Customer[];
  perMonthNotEmpty: boolean = false;
  forCountry = '';

  errorMessage: string;
  currentDate: Date = new Date();

  columnsToDisplay = ['First Name', 'Points for Current Month', 'Multiplier'];

  constructor(public customerService: CustomerService,
              public sessionService: SessionService) {
    this.sessionService.isLoggedIn.subscribe(value => {
      this.refresh();
    });
  }

  ngOnInit() {
    this.refresh();
  }

  refresh() {
    this.customerService.retrieveCustomersBySpendingPerMonth().subscribe(
      response => {
        this.customersLeaderboardPerMonth = response.customerEntities;
        if (this.customersLeaderboardPerMonth.length != 0) {
          this.perMonthNotEmpty = true;
          this.customersLeaderboardPerMonthMax5 = this.filterCustomerByCountry(this.customersLeaderboardPerMonth,
            this.customersLeaderboardPerMonthMax5);
        }

        this.customersLeaderboardPerMonthMax5 = this.customersLeaderboardPerMonthMax5.filter(customer => customer.pointsForCurrentMonth > 0);
        // console.log('inside index.component.ts! customerSpendingPerMonth: '
        //  + this.customersLeaderboardPerMonthMax5.length);
      },
      error => {
        console.log('********** IndexComponent.ts: customerPerMonth ' + error);
      }
    );
  }


  filterCustomerByCountry(customers: Customer[], customersNew: Customer[]) {
    customersNew = [];
    for (let cust of customers) {
      if (customersNew.length <= 5) {
        if (this.sessionService.getCurrentCustomer() == null) {
          customersNew.push(cust);
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
    if (this.sessionService.getIsLogin() === false) {
      return '(Global)';
    } else {
      return `(${this.sessionService.getCurrentCustomer().country})`;
    }
  }


}

