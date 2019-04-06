import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { LoginDialogComponent } from "../login-dialog/login-dialog.component";
import { CategoryService } from "../category.service";
import { Category } from '../category';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  rootCategories: Category[];

  constructor(public dialog: MatDialog,
    public categoryService: CategoryService) { 

      this.rootCategories = [];
 
    }

  ngOnInit() {
    this.getCategories();
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(LoginDialogComponent, {

    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  getCategories() {
    this.categoryService.getCategories().subscribe(
      response => {
        this.rootCategories = response.categoryEntities;  
      }
    )
  }

}
