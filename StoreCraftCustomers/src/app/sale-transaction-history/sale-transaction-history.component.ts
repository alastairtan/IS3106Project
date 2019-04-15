import { Component, OnInit, Input } from '@angular/core';
import { SaleTransactionService } from '../sale-transaction.service';
import { SaleTransaction } from '../saleTransaction';
import { animate, state, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-sale-transaction-history',
  templateUrl: './sale-transaction-history.component.html',
  styleUrls: ['./sale-transaction-history.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0', display: 'none' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class SaleTransactionHistoryComponent implements OnInit {

  private saleTransactions: SaleTransaction[];
  private columnsToDisplay = ['saleTransactionId', 'totalLineItem', 'totalQuantity', 'totalAmount'];
  private displayedColumns = ['productImage', 'productName', 'quantity', 'sub-total'];
  private expandedSaleTransaction;

  @Input()
  customerId: number;

  constructor(private saleTransactionService: SaleTransactionService,
  ) { }

  ngOnInit() {
    this.saleTransactionService.retrieveAllTransactions().subscribe(response => {
      this.saleTransactions = response.saleTransactionEntities;
    }
    )
  }

  format(currency: number) {
    return (new Intl.NumberFormat('en-SG', { style: 'currency', currency: 'SGD' }).format(currency));
  }
}
