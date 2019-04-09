import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {Category} from '../category';
import { SessionService } from '../session.service';

@Component({
  selector: 'app-category-menu-item',
  templateUrl: './category-menu-item.component.html',
  styleUrls: ['./category-menu-item.component.css']
})
export class CategoryMenuItemComponent implements OnInit {
  @Input() categoryItems : Category[];

  @ViewChild('childMenu') public childMenu;

  constructor(private sessionService: SessionService) { }

  ngOnInit() {
  }

  selectCategory(categoryId: number): void{
    this.sessionService.setSelectedCategory(categoryId);
    console.log(categoryId);
  }

}
