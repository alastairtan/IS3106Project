import {Component, Input, OnInit} from '@angular/core';
import {Product} from '../product';
import {Category} from '../category';
import {CommunityGoal} from '../community-goal';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent implements OnInit {

  @Input()
  object: Object;

  product: Product;

  category: Category;

  communityGoal: CommunityGoal;

  constructor() {
  }

  ngOnInit() {

    const productEg: Product = new Product();

    const categoryEg: Category = new Category();

    const communityGoalEg: CommunityGoal = new CommunityGoal();

    console.log('INIT');
    console.log(this.object);
    // @ts-ignore
    if (this.object.productId != null) {
      this.product = <Product>this.object;
    } else {
      // @ts-ignore
      if (this.object.categoryId != null) {
        this.category = <Category>this.object;
      } else {
        // @ts-ignore
        if (this.object.communityGoalId != null) {
          this.communityGoal = <CommunityGoal>this.object;
        }
      }
    }

  }

  format(number: number) {
    return (new Intl.NumberFormat('en-SG', {style: 'currency', currency: 'SGD'}).format(number));
  }

  getTagEntityNamesForProduct(product: Product) {
    // @ts-ignore
    if (product.tagEntities == null || product.tagEntities.length == 0) {
      return '- ';
    }

    let names: String = '';

    // @ts-ignore
    product.tagEntities.forEach(tagEntity => {
      names = names.concat(tagEntity.name + ', ');
    });

    return names.slice(0, names.length - 2);
  }

}
