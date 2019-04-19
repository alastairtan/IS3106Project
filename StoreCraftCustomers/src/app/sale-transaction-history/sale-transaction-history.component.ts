import {Component, Input, OnInit} from '@angular/core';
import {SaleTransactionService} from '../sale-transaction.service';
import {SaleTransaction} from '../saleTransaction';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {DiscountCode} from '../discount-code';

@Component({
  selector: 'app-sale-transaction-history',
  templateUrl: './sale-transaction-history.component.html',
  styleUrls: ['./sale-transaction-history.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0', display: 'none'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class SaleTransactionHistoryComponent implements OnInit {

  private saleTransactions: SaleTransaction[];
  private columnsToDisplay = ['saleTransactionId', 'totalLineItem', 'totalQuantity', 'totalAmount', 'discountCodeUsed'];
  private displayedColumns = ['productImage', 'productName', 'quantity', 'sub-total'];
  private expandedSaleTransaction;

  @Input()
  customerId: number;

  constructor(private saleTransactionService: SaleTransactionService,
  ) {
  }

  ngOnInit() {
    this.saleTransactionService.retrieveSaleTransactionByCustomerId().subscribe(response => {
        this.saleTransactions = response.saleTransactionEntities;
      }
    );
  }

  getProductsMessage(discountCodeEntity: DiscountCode) {
    let message = '(';
    if (discountCodeEntity.discountCodeTypeEnum.toString() == 'FLAT') {
      message += '$' + discountCodeEntity.discountAmountOrRate + ' Off ';
    } else if (discountCodeEntity.discountCodeTypeEnum.toString() == 'PERCENTAGE') {
      message += discountCodeEntity.discountAmountOrRate + '% Off ';
    }
    if (discountCodeEntity.productEntities == null || discountCodeEntity.productEntities.length == 0) {
      message += 'Entire Cart)';
    } else {
      discountCodeEntity.productEntities.forEach(product => {
        message += product.name + ', ';
      });
      message = message.slice(0, message.length - 2);
      message += ')';
    }
    return message;
  }

  format(currency: number) {
    return (new Intl.NumberFormat('en-SG', {style: 'currency', currency: 'SGD'}).format(currency));
  }
}
