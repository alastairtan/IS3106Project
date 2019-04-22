import { Component, OnInit, Input } from '@angular/core';
import { DiscountCodeService } from '../discount-code.service';
import { SessionService } from '../session.service';
import { DiscountCode } from '../discount-code';
import { animate, state, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-discount-code-table',
  templateUrl: './discount-code-table.component.html',
  styleUrls: ['./discount-code-table.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0', display: 'none' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class DiscountCodeTableComponent implements OnInit {

  @Input()
  customerId: number;

  discountCodes: DiscountCode[];

  errorMessage: string;

  columnsToDisplay = ['Discount Code', 'Type', 'From', 'To', 'Products'];

  expandedDc: DiscountCode | null;

  productColumnsToDisplay = ["Product Image", "Product Name", "Price"]

  constructor(
    public sessionService: SessionService,
    public dcService: DiscountCodeService
  ) {
  }

  ngOnInit() {
    this.getDiscountCodes();
  }

  getDiscountCodes() {
    this.dcService.retrieveDiscountCodesForCustomer(this.customerId).subscribe(response => {
      //this.discountCodes = response.discountCodeEntities;
      let now: Date = new Date();
      this.discountCodes = [];
      response.discountCodeEntities.forEach(discountCode => {
        console.log(discountCode.endDate)
        let endDate: Date = new Date(discountCode.endDate);
        endDate = new Date(endDate.toLocaleDateString());
        if (endDate > now && discountCode.numAvailable > 0) {
          this.discountCodes.push(discountCode)
        }
        console.log(this.discountCodes);
      })
    }, error => {
      this.errorMessage = error
    })
  }

  format(currency: number) {
    return (new Intl.NumberFormat('en-SG', { style: 'currency', currency: 'SGD' }).format(currency));
  }



}
