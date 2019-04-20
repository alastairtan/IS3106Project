import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { IndexComponent } from './index/index.component';

import { ViewProductsComponent } from './view-products/view-products.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ViewProductDetailsComponent } from './view-product-details/view-product-details.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { CommunityGoalsComponent } from './community-goals/community-goals.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';

const routes: Routes = [
  { path: '', redirectTo: '/index', pathMatch: 'full' },
  { path: 'index', component: IndexComponent },
  { path: 'profile/:customerId', component: UserProfileComponent},
  { path: 'viewProductsByCategory/:categoryId', component: ViewProductsComponent },
  { path: 'product/:productId', component: ViewProductDetailsComponent},
  { path: 'cart/:userId', component: ShoppingCartComponent},
  { path: 'communitygoals', component: CommunityGoalsComponent},
  { path: 'leaderboard', component: LeaderboardComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
