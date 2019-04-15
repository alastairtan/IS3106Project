import { Product } from './product';

export class CartItem {

    productEntity : Product;
    quantity : number;
    subTotal : number;
    unitPrice : number;
    serialNumber : number;

    constructor(serialNumber?: number, productEntity?: Product, quantity?: number,
        unitPrice?: number, subTotal?: number)
    {
        this.productEntity = productEntity;
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.unitPrice = unitPrice;
        this.serialNumber = serialNumber;
    }
}