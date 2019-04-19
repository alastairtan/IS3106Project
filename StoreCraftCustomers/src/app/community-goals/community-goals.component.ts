import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource, MatSort, MatPaginator, MatDialog, MatDialogRef} from '@angular/material';
import {SessionService} from '../session.service';
import {CommunityGoalService} from '../community-goal.service';
import {CommunityGoal} from '../community-goal';
import {CountryService} from '../country.service';

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
  country: String;
  countries;
  communityGoals: CommunityGoal[];
  errorMessage: String;
  originalCommunityGoals: CommunityGoal[];

  constructor(public sessionService: SessionService,
              public communityGoalService: CommunityGoalService,
              public countryService: CountryService,
              public dialog: MatDialog) {
  }

  ngOnInit() {
    this.communityGoalService.retrieveAllCommunityGoals().subscribe(
      response => {
        this.communityGoals = response.communityGoalEntities;

        // Moved the processing to backend
        // this.communityGoals = this.communityGoals.filter(communityGoal => communityGoal.completed || 
        //   (new Date(Date.parse(`${communityGoal.endDate}`)) > new Date) )

        this.originalCommunityGoals = this.communityGoals;
        for (const communityGoal of this.communityGoals) {
          if (this.communityGoals.length != 0) {

            // communityGoal.targetPoints = (communityGoal.currentPoints*100)/communityGoal.targetPoints;
            console.log('********** CommunityGoalComponent.ts: ' + communityGoal.rewardPercentage);
          }
        }
      }
      , error => {
        console.log('********** CommunityGoalComponent.ts: ', error);
      }
    );
    this.countryService.getCountries().subscribe(
      response => {
        this.countries = response;
      }, error => {
        console.log('********** Getting countries in communitygoal: ', error);
      }
    );
  }


  public doFilter(value: string) {

    const temp = [];
    if (this.communityGoals.length != 0 && value.length > 0) {
      for (const communityGoal of this.communityGoals) {
        if (communityGoal.country.toLowerCase().includes(value.toLowerCase())) {

          temp.push(communityGoal);
        }
      }
      this.communityGoals = temp;
    } else {
      this.communityGoals = this.originalCommunityGoals;
    }
  }

  resetView() {
    this.communityGoals = this.originalCommunityGoals;
  }

}

