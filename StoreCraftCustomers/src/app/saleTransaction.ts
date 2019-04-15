import { Customer } from './customer';
import { CartItem } from './cartItem';


export class SaleTransaction {

    totalLineItem: number;
    totalQuantity: number;
    totalAmount: number;
    transactionDateTime : Date;
    voidRefund : boolean;
    customerEntity : Customer;
    saleTransactionLineItemEntities : CartItem[];


    constructor( totalLineItem? : number,
        totalQuantity? : number,
        totalAmount? : number,
        transactionDateTime? : Date,
        voidRefund? : boolean,
        customerEntity? : Customer,
        saleTransactionLineItemEntities? : CartItem[] ) {

        this.totalLineItem = totalLineItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.transactionDateTime = transactionDateTime;
        this.voidRefund = voidRefund;
        this.customerEntity = customerEntity;
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }
}