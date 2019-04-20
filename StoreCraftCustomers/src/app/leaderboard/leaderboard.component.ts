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

  currentCustomer: Customer;
  loggedIn: boolean;

  constructor(public customerService: CustomerService,
    public sessionService: SessionService) { }

  ngOnInit() {

    this.customerService.retrieveCustomersBySpendingTotal().subscribe(
      response => {
        this.dataSource.data = response.customerEntities;
        this.currentCustomer = this.sessionService.getCurrentCustomer();
        let rank = 1;
        for (const customer of this.dataSource.data) {
          customer.rank = rank++;
          if (this.sessionService.getIsLogin() && this.currentCustomer.email === customer.email) {
            console.log(this.sessionService.getCurrentCustomer());
            this.currentCustomer = customer;
            this.loggedIn = true;
          }
        }
        setTimeout(() => {
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
        });
      }
    )
  }


  public doFilter = (value: string) => {
    this.dataSource.filter = value.trim().toLocaleLowerCase();
  }

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
    )
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
    )
  }


}
