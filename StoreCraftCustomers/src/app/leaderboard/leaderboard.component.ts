import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatPaginator } from '@angular/material';
import { Customer } from '../customer';
import { CustomerService } from '../customer.service';

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

  constructor(public customerService : CustomerService) { }

  ngOnInit() {
    this.customerService.retrieveCustomersBySpendingTotal().subscribe(
      response => {
          this.dataSource.data = response.customerEntities;
          let rank = 1;
          for(const customer of this.dataSource.data){
            customer.rank = rank++;
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

}
