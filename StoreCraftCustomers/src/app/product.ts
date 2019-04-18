import { Category } from './category';

export class Product {
    productId: number;
    skuCode: string;
    name: string;
    description: string;
    quantityOnHand: number;
    reorderQuantity: number;
    unitPrice: number;
    isScavengerHuntPrize: boolean;
    productImageUrl: string;

    categoryEntity: Category;
    discountCodeEntities: object;
    tagEntities: object;
    reviewEntities: object;

    constructor(skuCode?: string, name?: string, description?: string, quantityOnHand?: number,
        reorderQuantity?: number, unitPrice?: number, isScavengerHuntPrize?: boolean,
        productImageUrl?: string ) {

        this.skuCode = skuCode;
        this.name = name;
        this.description = description;
        this.quantityOnHand = quantityOnHand;
        this.reorderQuantity = reorderQuantity;
        this.unitPrice = unitPrice;
        this.isScavengerHuntPrize = isScavengerHuntPrize;
        this.productImageUrl = productImageUrl;
    }
}
