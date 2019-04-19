import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {LoginDialogComponent} from '../login-dialog/login-dialog.component';
import {CategoryService} from '../category.service';
import {Category} from '../category';
import {SessionService} from '../session.service';
import {ActivatedRoute, Router} from '@angular/router';
import {RegisterDialogComponent} from '../register-dialog/register-dialog.component';
import {LocalService} from '../local.service';
import {ProductService} from '../product.service';
import {Product} from '../product';
import {CommunityGoal} from '../community-goal';
import {CommunityGoalService} from '../community-goal.service';
import {ShoppingCartComponent} from '../shopping-cart/shopping-cart.component';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
  providers: [ShoppingCartComponent]
})
export class HeaderComponent implements OnInit {

  rootCategories: Category[];

  allProducts: Product[];

  allCategories: Category[];

  allCommunityGoals: CommunityGoal[];

  allStuffToFilter: Object[];

  filteredStuff: Object[];

  searchString: string;

  constructor(public dialog: MatDialog,
              public categoryService: CategoryService,
              public sessionService: SessionService,
              public router: Router,
              public localService: LocalService,
              private productService: ProductService,
              private communityGoalService: CommunityGoalService,
              private shoppingCartComponent: ShoppingCartComponent) {

    this.rootCategories = [];
    this.allStuffToFilter = [];
  }

  ngOnInit() {
    this.getCategories();
    this.getProducts();
    this.getAllCategoriesList();
    this.getAllCommunityGoals();
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(LoginDialogComponent, {});

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  openRegisterDialog(): void {
    const dialogRef = this.dialog.open(RegisterDialogComponent, {});

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  getCategories() {
    this.categoryService.getCategories().subscribe(
      response => {
        this.rootCategories = response.categoryEntities;
        // console.log(this.rootCategories);
      }
    );
  }

  getProducts() {
    this.productService.getAllProducts().subscribe(response => {
      this.allProducts = response.productEntities;
      this.allStuffToFilter = this.allStuffToFilter.concat(this.allProducts);
    });
  }

  getAllCategoriesList() {
    this.categoryService.retrieveAllCategoriesList().subscribe(response => {
      this.allCategories = response.categoryEntities;
      this.allStuffToFilter = this.allStuffToFilter.concat(this.allCategories);
    });
  }

  getAllCommunityGoals() {
    this.communityGoalService.retrieveAllCommunityGoals().subscribe(response => {
      this.allCommunityGoals = response.communityGoalEntities;
      this.allStuffToFilter = this.allStuffToFilter.concat(this.allCommunityGoals);
    });
  }

  doFilter(value: string) {
    if (value.length > 0) {
      this.filteredStuff = this.filterByValue(this.allStuffToFilter, value);
    } else {
      this.filteredStuff = [];
    }
  }

  filterByValue(array: Object[], searchString: string) {
    return array.filter(o =>
      this.objectPropertiesHasValue(o, searchString));
  }

  objectPropertiesHasValue(object: Object, searchString: string): boolean {
    return Object.keys(object).some(k => {
      if (k !== 'description') {
        if (k === 'tagEntities') {
          console.log(object[k]);
          let found = false;
          for (let i = 0; i < object[k].length; i++) {
            if (this.objectPropertiesHasValue(object[k][i], searchString)) {
              found = true;
            }
          }
          return found;
        } else {
          return String(object[k]).toLowerCase().includes(searchString.toLowerCase());
        }
      }
    });
  }

  logout() {
    this.sessionService.setIsLogin(false);
    this.sessionService.setCurrentCustomer(null);
    this.router.navigateByUrl('/index');
  }


  format(currency: number) {
    return (new Intl.NumberFormat('en-SG', {style: 'currency', currency: 'SGD'}).format(currency));
  }

  getSubCategoryListInclParent(category: Category): Category[] {
    const categoryItems: Category[] = category.subCategoryEntities;
    const categoryEmptied: Category = JSON.parse(JSON.stringify(category));
    if (categoryItems.find(item => item.categoryId == categoryEmptied.categoryId) == null) {
      categoryEmptied.subCategoryEntities = [];
      categoryEmptied.name = 'All';
      categoryItems.push(categoryEmptied);
      console.log(categoryItems);
      return categoryItems;
    } else {
      return categoryItems;
    }
  }

}
