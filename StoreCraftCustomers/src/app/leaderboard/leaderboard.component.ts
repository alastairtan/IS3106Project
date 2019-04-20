import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatPaginator } from '@angular/material';
import { Customer } from '../customer';
import { CustomerService } from '../customer.service';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrls: ['./leaderboard.component.css']
})
export class LeaderboardComponent implements OnInit {

  public displayedColumns = ['rank', 'email', 'totalPoints', 'country'];

  public dataSource = new MatTableDataSource<Customer>();
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  currentCustomerRank: number;


  constructor(public customerService: CustomerService,
              public sessionService: SessionService) {
    this.sessionService.isLoggedIn.subscribe(value => {
      this.ngOnInit();
    })
  }

  ngOnInit() {

    this.customerService.retrieveCustomersBySpendingTotal().subscribe(
      response => {
        this.dataSource.data = response.customerEntities;
        let rank = 1;
        for (const customer of this.dataSource.data) {
          customer.rank = rank++;
        }
        this.getCustomerRank();
        setTimeout(() => {
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        });
      }
    );
  }

  getCustomerRank() {
    for (const customer of this.dataSource.data) {
      if (customer.customerId === this.sessionService.getCurrentCustomer().customerId) {
        this.currentCustomerRank = customer.rank;
      }
    }
  }

  getCurrentReward(): string {
    let message = '';
    if (this.currentCustomerRank <= 2) {
      message += '20% Off';
    } else if (this.currentCustomerRank <= 4) {
      message += '15% Off';
    } else if (this.currentCustomerRank <= 6) {
      message += '10% Off';
    } else {
      message += '5% Off';
    }

    message += ' Store-wide Discount Code';

    return message;
  }

  getNextReward(): string {
    let message = '';

    if (this.currentCustomerRank <= 2) {
      message += 'You have reached the highest tier of rewards. Keep it up!';
    } else if (this.currentCustomerRank <= 4) {
      message += '20% Off Store-wide Discount Code at Rank 2';
    } else if (this.currentCustomerRank <= 6) {
      message += '15% Off Store-Wide Discount Code at Rank 4';
    } else {
      message += '10% Off Store-Wide Discount Code at Rank 6';
    }

    return message;
  }


  public doFilter = (value: string) => {
    this.dataSource.filter = value.trim().toLocaleLowerCase();
  };

  getAllTimeTopCustomers() {
    this.customerService.retrieveCustomersBySpendingTotal().subscribe(
      response => {
        this.dataSource.data = response.customerEntities;
        let rank = 1;
        for (const customer of this.dataSource.data) {
          customer.rank = rank++;
        }
        setTimeout(() => {
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        });
      }
    );
  }

  getThisMonthTopCustomers() {
    this.customerService.retrieveCustomersBySpendingPerMonth().subscribe(
      response => {
        this.dataSource.data = response.customerEntities;
        let rank = 1;
        for (const customer of this.dataSource.data) {
          customer.rank = rank++;
        }
        setTimeout(() => {
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        });
      }
    );
  }


}
