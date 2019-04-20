import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from '@angular/material';


@Component({
  selector: 'app-scavenger-prize-dialog',
  templateUrl: './scavenger-prize-dialog.component.html',
  styleUrls: ['./scavenger-prize-dialog.component.css']
})
export class ScavengerPrizeDialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<ScavengerPrizeDialogComponent>
  ) { }

  ngOnInit() {
  }

  exit(): void {
    this.dialogRef.close();
  }

}
