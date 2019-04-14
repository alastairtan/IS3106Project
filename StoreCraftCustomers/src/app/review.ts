import { Customer } from './customer';
import { Staff } from './staff';

export class Review{
    reviewId: number;
    content: string;
    productRating: number;
    reviewDate: Date;
    customerEntity: Customer;
    staffEntity: Staff;
    parentReviewId: number;


    constructor(reviewId?:number, content?:string, productRating?:number,
        reviewDate?:Date, customerEntity?:Customer, staffEntity?:Staff, parentReviewId?:number  ){

        this.reviewId = reviewId;
        this.content = content;
        this.productRating = productRating;
        this.reviewDate = reviewDate;
        this.customerEntity = customerEntity;
        this.staffEntity = staffEntity;
        this.parentReviewId = parentReviewId;
    }
}