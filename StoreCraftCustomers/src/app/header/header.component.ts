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
  allCategories: Category[];
  subCategories: Category[];


  constructor(public dialog: MatDialog,
    public categoryService: CategoryService) { 

      this.rootCategories = [];
      this.allCategories = [];
      this.subCategories = [];
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
        let categories: Category[] = response.categoryEntities;
        
        categories.forEach(c => {
          this.allCategories.push(c);
          if (c.parentCategoryEntity == null) {
            this.rootCategories.push(c);
          }
        })
      }

    )
  }

  getSubCategoriesFor(parentCategory: Category): void {
    this.subCategories = [];
    this.allCategories.forEach(c => {
      if (c.parentCategoryEntity != null && c.parentCategoryEntity.categoryId === parentCategory.categoryId){
        this.subCategories.push(c);
      }
    })
  }


}
