import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscountCodeTableComponent } from './discount-code-table.component';

describe('DiscountCodeTableComponent', () => {
  let component: DiscountCodeTableComponent;
  let fixture: ComponentFixture<DiscountCodeTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiscountCodeTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiscountCodeTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
