import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { CustomerService} from '../customer.service';
import {LoginDialogComponent} from '../login-dialog/login-dialog.component';

@Component({
  selector: 'app-tier-info-dialog',
  templateUrl: './tier-info-dialog.component.html',
  styleUrls: ['./tier-info-dialog.component.css']
})
export class TierInfoDialogComponent implements OnInit {

  urlList: String[] = ['https://opgg-static.akamaized.net/images/medals/bronze_1.png',
    'https://opgg-static.akamaized.net/images/medals/silver_1.png',
    'https://opgg-static.akamaized.net/images/medals/gold_1.png',
    'https://opgg-static.akamaized.net/images/medals/platinum_1.png',
    'https://opgg-static.akamaized.net/images/medals/diamond_1.png',
    'https://opgg-static.akamaized.net/images/medals/master_1.png',
    'https://opgg-static.akamaized.net/images/medals/grandmaster_1.png',
  ]

  tiers: String[] = ['BRONZE', 'SILVER', 'GOLD', 'PLATINUM', 'DIAMOND', 'MASTER', 'GRANDMASTER'];

  pointsRequiredList: number[] = [0, 3000, 6000, 9000, 12000, 15000, 18000];

  multipliers: number[] = [1, 1.2, 1.4, 1.6, 2.0, 2.5, 3.0]

  constructor(
    public dialogRef: MatDialogRef<LoginDialogComponent>,
    public customerService: CustomerService,
  ) { }

  ngOnInit() {
  }

  exit(): void {
    this.dialogRef.close();
  }

}
