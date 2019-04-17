
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

  /*public columnsToDisplay=['communityGoalId','goalTitle','description','percentageLeft',
  'startDate','endDate','rewardPercentage','completed'];

  public dataSource = new MatTableDataSource<CommunityGoal>();
  */
  country : String;
  countries;
  communityGoals : CommunityGoal[];


  constructor(public sessionService : SessionService,
              public communityGoalService : CommunityGoalService,
              public countryService : CountryService,
              public dialog : MatDialog) { }

  ngOnInit() {
      this.communityGoalService.retrieveAllCommunityGoals().subscribe(
      response =>{
        this.communityGoals = response.communityGoalEntities;
        for(let communityGoal of this.communityGoals){
          if(this.communityGoals.length != 0) {
            
            communityGoal.targetPoints = (communityGoal.currentPoints*100)/communityGoal.targetPoints;
            console.log('********** CommunityGoalComponent.ts: ' + communityGoal.rewardPercentage);
          }
      }
      }
      ,error =>{
        console.log('********** CommunityGoalComponent.ts: ', error);
      }
    )
      this.countryService.getCountries().subscribe(
        response =>{
          this.countries = response;
        }, error =>{
          console.log('********** Getting countries in communitygoal: ', error);
        }
      )
  }


  public doFilter (value: string) {
    console.log("whats in here : " + value);
  }

}

