import 'hammerjs';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule, MatSortModule } from '@angular/material';
import { HttpClientModule } from '@angular/common/http';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatDialogModule } from '@angular/material/dialog';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { MatPaginatorModule } from '@angular/material/paginator';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSelectModule } from '@angular/material/select';
import { MatSliderModule } from '@angular/material/slider';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { TextFieldModule } from '@angular/cdk/text-field';
import { StarRatingModule } from 'angular-star-rating';
import { MatBadgeModule } from '@angular/material/badge';



import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import { ViewProductsComponent } from './view-products/view-products.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { SaleTransactionHistoryComponent } from './sale-transaction-history/sale-transaction-history.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { CommunityGoalsComponent } from './community-goals/community-goals.component';
import { ViewProductDetailsComponent } from './view-product-details/view-product-details.component';
import { IndexComponent } from './index/index.component';
import { ScavengerHuntComponent } from './scavenger-hunt/scavenger-hunt.component';
import { MatInputModule, MatInput } from '@angular/material/input';
import { LoginDialogComponent } from './login-dialog/login-dialog.component';
import { CategoryBarComponent } from './category-bar/category-bar.component';
import { CategoryMenuItemComponent } from './category-menu-item/category-menu-item.component';
import { ProductCardComponent } from './product-card/product-card.component';
import { RegisterDialogComponent } from './register-dialog/register-dialog.component';
import { ReviewChainComponent } from './review-chain/review-chain.component';
import { DiscountCodeTableComponent } from './discount-code-table/discount-code-table.component';
import { WriteReviewComponent } from './write-review/write-review.component';



@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    LeaderboardComponent,
    ViewProductsComponent,
    UserProfileComponent,
    SaleTransactionHistoryComponent,
    ShoppingCartComponent,
    CommunityGoalsComponent,
    ViewProductDetailsComponent,
    IndexComponent,
    ScavengerHuntComponent,
    LoginDialogComponent,
    CategoryBarComponent,
    CategoryMenuItemComponent,
    ProductCardComponent,
    RegisterDialogComponent,
    ReviewChainComponent,
    WriteReviewComponent,
    DiscountCodeTableComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    MatTableModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatGridListModule,
    MatToolbarModule,
    MatInputModule,
    MatIconModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatMenuModule,
    MatDialogModule,
    MatListModule,
    MatCardModule,
    MatPaginatorModule,
    FlexLayoutModule,
    MatSidenavModule,
    MatExpansionModule,
    MatCheckboxModule,
    MatSelectModule,
    MatSliderModule,
    MatChipsModule,
    MatProgressBarModule,
    MatSortModule,
    TextFieldModule,
    StarRatingModule.forRoot(),
    MatBadgeModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [LoginDialogComponent, RegisterDialogComponent]
})
export class AppModule { }
