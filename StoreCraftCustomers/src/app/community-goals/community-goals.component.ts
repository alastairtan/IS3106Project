
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatSort, MatPaginator, MatDialog, MatDialogRef} from '@angular/material';
import { SessionService } from '../session.service';
import { CommunityGoalService } from '../community-goal.service';
import {CommunityGoal} from '../community-goal'
import { CountryService } from '../country.service';

@Component({
  selector: 'app-community-goals',
  templateUrl: './community-goals.component.html',
  styleUrls: ['./community-goals.component.css']
})
export class CommunityGoalsComponent implements OnInit {

  public columnsToDisplay=['communityGoalId','goalTitle','description','targetPoints','currentPoints',
  'startDate','endDate','rewardPercentage','completed'];

  public dataSource = new MatTableDataSource<CommunityGoal>();
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  country : String;
  countries;

  constructor(public sessionService : SessionService,
              public communityGoalService : CommunityGoalService,
              public countryService : CountryService,
              public dialog : MatDialog) { }

  ngOnInit() {
      this.getCommunityGoals();
      this.countryService.getCountries().subscribe(
        response =>{
          this.countries = response;
        }, error =>{
          console.log('********** Getting countries in communitygoal: ', error);
        }
      )
  }

  public getCommunityGoals(){
    this.communityGoalService.retrieveAllCommunityGoals().subscribe(
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

  public doFilter (value: string) {
    console.log("whats in here : " + value);
    this.dataSource.filter = value.trim().toLocaleLowerCase();
  }

}

