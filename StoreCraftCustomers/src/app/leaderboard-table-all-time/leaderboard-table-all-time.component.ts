import { Component, OnInit } from '@angular/core';
import { Customer } from '../customer';
import { CustomerService } from '../customer.service';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-leaderboard-table-all-time',
  templateUrl: './leaderboard-table-all-time.component.html',
  styleUrls: ['./leaderboard-table-all-time.component.css']
})
export class LeaderboardTableAllTimeComponent implements OnInit {

  customersLeaderboardTotal: Customer[];
  customersLeaderboardTotalMax5: Customer[];
  perMonthNotEmpty: boolean = false;

  errorMessage: string;
  currentDate: Date = new Date();

  columnsToDisplay = ['First Name', 'Points for Current Month', 'Multiplier'];

  constructor(public customerService: CustomerService,
              public sessionService: SessionService) { }

  ngOnInit() {
    this.customerService.retrieveCustomersBySpendingTotal().subscribe(
      response => {
        this.customersLeaderboardTotal = response.customerEntities;
        if(this.customersLeaderboardTotal.length != 0) {
          this.perMonthNotEmpty = true;
          this.customersLeaderboardTotalMax5 = this.filterCustomerByCountry(this.customersLeaderboardTotal, 
            this.customersLeaderboardTotalMax5)
        }
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
     for(var cust of customers) {
       if(customersNew.length <= 5) {
         if(cust.country == this.sessionService.getCurrentCustomer().country) {
           customersNew.push(cust);
         } 
       } else {
         break;
       }
     }
     return customersNew;
   }

}

