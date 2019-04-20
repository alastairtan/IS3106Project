import { MembershipTierEnum } from './MembershipTierEnum.enum';

export class Customer {

    customerId: number;
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    membershipTierEnum: MembershipTierEnum;
    country: string;
    totalPoints: number;
    remainingPoints: number;
    pointsForCurrentMonth: number;
    multiplier: number;
    profilePicUrl: string;
    tierUrl: string;
    tierMessage: string;
    rank : number;

    constructor(customerId?: number, firstName?: string, lastName?: string, email?: string, password?: string,
        membershipTierEnum?: MembershipTierEnum, country?: string,
        totalPoints?: number, remainingPoints?: number, pointsForCurrentMonth?: number,
        multiplier?: number, profilePicUrl?: string) {

        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.membershipTierEnum = membershipTierEnum;
        this.country = country;
        this.totalPoints = totalPoints;
        this.remainingPoints = remainingPoints;
        this.pointsForCurrentMonth = pointsForCurrentMonth;
        this.multiplier = multiplier;
        this.profilePicUrl = profilePicUrl;

    }
}