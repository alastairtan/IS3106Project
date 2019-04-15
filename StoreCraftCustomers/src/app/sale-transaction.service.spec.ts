import { TestBed } from '@angular/core/testing';

import { SaleTransactionService } from './sale-transaction.service';

describe('SaleTransactionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SaleTransactionService = TestBed.get(SaleTransactionService);
    expect(service).toBeTruthy();
  });
});
