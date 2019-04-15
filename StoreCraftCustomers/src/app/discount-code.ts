import { Product } from './product';
import { DiscountCodeTypeEnum } from './DiscountCodeTypeEnum';

export class DiscountCode{
    discountCodeId: number;
    discountAmountOrRate: number;
    discountCode: string;
    discountCodeTypeEnum: DiscountCodeTypeEnum;
    numAvailable: number;
    productEntities: Product[];
    startDate: Date;
    endDate: Date;

    constructor(discountCodeId?:number, discountAmountOrRate?:number, discountCode?:string, 
        numAvailable?:number, productEntities?:Product[], startDate?:Date, endDate?:Date, ){

        this.discountCodeId = discountCodeId;
        this.discountAmountOrRate = discountAmountOrRate;
        this.discountCode = discountCode;
        this.numAvailable = numAvailable;
        this.productEntities = productEntities;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}