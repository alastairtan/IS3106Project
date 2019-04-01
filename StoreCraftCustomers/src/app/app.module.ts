import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';

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
    ScavengerHuntComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    MatGridListModule,
    MatToolbarModule,
    MatInputModule,
    MatIconModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatMenuModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
