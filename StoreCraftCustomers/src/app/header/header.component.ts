import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { LoginDialogComponent } from "../login-dialog/login-dialog.component";
import { CategoryService } from "../category.service";
import { Category } from '../category';
import { SessionService } from '../session.service';
import { ActivatedRoute, Router } from '@angular/router';
import { RegisterDialogComponent } from '../register-dialog/register-dialog.component';
import { LocalService } from '../local.service';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  rootCategories: Category[];

  constructor(public dialog: MatDialog,
    public categoryService: CategoryService,
    public sessionService:SessionService,
    public router: Router,
    public localService: LocalService) { 

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

  openRegisterDialog(): void {
    const dialogRef = this.dialog.open(RegisterDialogComponent, {

    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  getCategories() {
    this.categoryService.getCategories().subscribe(
      response => {
        this.rootCategories = response.categoryEntities;  
        //console.log(this.rootCategories);
      }
    )
  }

  logout(){
    this.sessionService.setIsLogin(false);
    this.sessionService.setCurrentCustomer(null);
    this.router.navigateByUrl('/index');
  }

  searchForProduct() {
    
  }

  format(currency: number) {
    return (new Intl.NumberFormat('en-SG', { style: 'currency', currency: 'SGD' }).format(currency));
  }

}
