import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatPaginator} from '@angular/material';
import { SessionService } from '../session.service';
import { CommunityGoalService } from '../community-goal.service';
import {CommunityGoal} from '../community-goal'

@Component({
  selector: 'app-community-goals',
  templateUrl: './community-goals.component.html',
  styleUrls: ['./community-goals.component.css']
})
export class CommunityGoalsComponent implements OnInit {

  public columnsToDisplay=['communityGoalId','goalTitle','description','targetPoints','currentPoints',
  'startDate','endDate'];

  public dataSource = new MatTableDataSource<CommunityGoal>();
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  country : String;

  constructor(public sessionService : SessionService,
              public communityGoalService : CommunityGoalService) { }

  ngOnInit() {
      this.getCommunityGoals();
  }

  public getCommunityGoals(){
    let country = this.sessionService.getCurrentCustomer().country;
    this.communityGoalService.retrieveCurrentCommunityGoalsByCountry(country).subscribe(
      response =>{
        this.dataSource = new MatTableDataSource<CommunityGoal>(response.communityGoalEntities);
        setTimeout(() => {
          this.dataSource.sort = this.sort;
        });
      }
      ,error =>{
        console.log('********** CommunityGoalComponent.ts: ', error);
      }
    )
  }

  ngAfterViewInit() : void{
    
    this.dataSource.paginator = this.paginator;
  }

  public doFilter = (value: string) => {
    this.dataSource.filter = value.trim().toLocaleLowerCase();
  }

}
