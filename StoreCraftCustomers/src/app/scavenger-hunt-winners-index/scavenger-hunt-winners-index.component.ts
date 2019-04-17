import { Component, OnInit } from '@angular/core';
import { ScavengerHunt } from '../scavengerHunt';
import { Customer } from '../customer';
import { ScavengerHuntService } from '../scavenger-hunt.service';
import { CustomerService } from '../customer.service';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-scavenger-hunt-winners-index',
  templateUrl: './scavenger-hunt-winners-index.component.html',
  styleUrls: ['./scavenger-hunt-winners-index.component.css']
})
export class ScavengerHuntWinnersIndexComponent implements OnInit {

  scavengerHunt: ScavengerHunt;
  scavengerHuntWinners: Customer[];
  isEmpty: boolean;

  errorMessage: string;
  currentDate: Date = new Date();

  columnsToDisplay = ['First Name', 'Membership Tier', 'Country'];


  constructor(public scavengerHuntService: ScavengerHuntService,
              public customerService: CustomerService,
              public sessionService: SessionService) { }

  ngOnInit() {
    this.scavengerHuntService.retrieveScavengerHuntForTheDay().subscribe(
      response => {
        this.scavengerHunt = response.scavengerHuntEntity;
        if(this.scavengerHunt != null ) {
          if(this.scavengerHunt.customerEntities.length != 0) {
            this.scavengerHuntWinners = this.scavengerHunt.customerEntities;
          }
          console.log('inside index.component.ts! scavengerHunt!' 
          + this.scavengerHunt.customerEntities.length);
        }
        console.log(this.currentDate);
      },
      error => {
        console.log('********** IndexComponent.ts: scavenger ' + error);
      }
      
    )
  }

}