import { Component, OnInit } from '@angular/core';
import {MatTableModule} from '@angular/material';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-community-goals',
  templateUrl: './community-goals.component.html',
  styleUrls: ['./community-goals.component.css']
})
export class CommunityGoalsComponent implements OnInit {

  public columnsToDisplay=["communityGoalId","communityGoalTitle","communityGoalDescription","communityGoalTargetPoints","communityGoalCurrentPoints",
  "communityGoalStartDate","communityGoalEndDate"];

  constructor(public sessionService) { }

  ngOnInit() {
  }

}
