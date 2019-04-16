import { Customer } from './customer';
import { CartItem } from './cartItem';
import { DiscountCode} from './discount-code';


export class SaleTransaction {

    totalLineItem: number;
    totalQuantity: number;
    totalAmount: number;
    transactionDateTime : Date;
    voidRefund : boolean;
    customerEntity : Customer;
    saleTransactionLineItemEntities : CartItem[];
    discountCodeEntity: DiscountCode;
    pointsToUse: number;


    constructor( totalLineItem? : number,
        totalQuantity? : number,
        totalAmount? : number,
        transactionDateTime? : Date,
        voidRefund? : boolean,
        customerEntity? : Customer,
        saleTransactionLineItemEntities? : CartItem[],
        discountCodeEntity? :DiscountCode,
        pointsToUse? :number) {

        this.totalLineItem = totalLineItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.transactionDateTime = transactionDateTime;
        this.voidRefund = voidRefund;
        this.customerEntity = customerEntity;
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
        this.discountCodeEntity = discountCodeEntity;
        this.pointsToUse = pointsToUse;
    }
}