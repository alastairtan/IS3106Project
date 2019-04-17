import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../customer.service';
import { SessionService } from '../session.service';
import { Customer } from '../customer';

@Component({
  selector: 'app-leaderboard-table',
  templateUrl: './leaderboard-table.component.html',
  styleUrls: ['./leaderboard-table.component.css']
})
export class LeaderboardTableComponent implements OnInit {

  customersLeaderboardPerMonth: Customer[];
  customersLeaderboardPerMonthMax5: Customer[];
  perMonthNotEmpty: boolean = false;

  errorMessage: string;
  currentDate: Date = new Date();

  columnsToDisplay = ['First Name', 'Points for Current Month', 'Multiplier'];

  constructor(public customerService: CustomerService,
              public sessionService: SessionService) { }

  ngOnInit() {
    this.customerService.retrieveCustomersBySpendingPerMonth().subscribe(
      response => {
        this.customersLeaderboardPerMonth = response.customerEntities;
        if(this.customersLeaderboardPerMonth.length != 0) {
          this.perMonthNotEmpty = true;
          this.customersLeaderboardPerMonthMax5 = this.filterCustomerByCountry(this.customersLeaderboardPerMonth, 
            this.customersLeaderboardPerMonthMax5)
        }
        console.log('inside index.component.ts! customerSpendingPerMonth: ' 
        + this.customersLeaderboardPerMonthMax5.length);
      },
      error => {
        console.log('********** IndexComponent.ts: customerPerMonth ' + error);
      }
    );
  }


  filterCustomerByCountry(customers: Customer[], customersNew: Customer[]) {
    customersNew = [];
     for(var cust of customers) {
       if(customersNew.length <= 5) {
        if(this.sessionService.getCurrentCustomer() == null) {
          if(cust.country == "Singapore") {
            customersNew.push(cust);
          }
        } else if (cust.country == this.sessionService.getCurrentCustomer().country) {
           customersNew.push(cust);
         } 
       } else {
         break;
       }
     }
     return customersNew;
   }

}

