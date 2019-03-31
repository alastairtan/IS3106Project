import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SaleTransactionHistoryComponent } from './sale-transaction-history.component';

describe('SaleTransactionHistoryComponent', () => {
  let component: SaleTransactionHistoryComponent;
  let fixture: ComponentFixture<SaleTransactionHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SaleTransactionHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SaleTransactionHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
