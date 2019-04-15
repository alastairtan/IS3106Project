import { Component, OnInit } from '@angular/core';
import { SaleTransactionService } from '../sale-transaction.service';
import { SaleTransaction } from '../saleTransaction';

@Component({
  selector: 'app-sale-transaction-history',
  templateUrl: './sale-transaction-history.component.html',
  styleUrls: ['./sale-transaction-history.component.css']
})
export class SaleTransactionHistoryComponent implements OnInit {

  private saleTransactions: SaleTransaction[];

  constructor(private saleTransactionService: SaleTransactionService,
  ) { }

  ngOnInit() {
    this.saleTransactionService.retrieveAllTransactions().subscribe(response => {
      this.saleTransactions = response;
    }
    )
  }
}
