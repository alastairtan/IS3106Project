import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from '@angular/material';
import {CustomerService} from '../customer.service';

@Component({
  selector: 'app-tier-info-dialog',
  templateUrl: './tier-info-dialog.component.html',
  styleUrls: ['./tier-info-dialog.component.css']
})
export class TierInfoDialogComponent implements OnInit {

  urlList: String[] = ['/assets/images/bronze_1.png',
    '/assets/images/silver_1.png',
    '/assets/images/gold_1.png',
    '/assets/images/platinum_1.png',
    '/assets/images/diamond_1.png',
    '/assets/images/master_1.png',
    '/assets/images/grandmaster_1.png',
  ];

  tiers: String[] = ['BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'DIAMOND', 'MASTER', 'GRANDMASTER'];

  pointsRequiredList: number[] = [0, 3000, 6000, 9000, 12000, 15000, 18000];

  multipliers: number[] = [1, 1.2, 1.4, 1.6, 2.0, 2.5, 3.0];

  constructor(
    public dialogRef: MatDialogRef<TierInfoDialogComponent>,
    public customerService: CustomerService,
  ) {
  }

  ngOnInit() {
  }

  exit(): void {
    this.dialogRef.close();
  }

}
