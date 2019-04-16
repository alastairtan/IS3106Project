import { RewardTypeEnum } from './RewardTypeEnum.enum';
import { Customer } from './customer';

export class ScavengerHunt {

    scavengerHuntId: number;
    rewardTypeEnum: RewardTypeEnum;
    scavengerHuntDate: Date;
    numWinnersRemaining: number;
    customerEntities: Customer[];

    constructor(scavengerHuntId?: number, rewardTypeEnum?: RewardTypeEnum, scavengerHuntDate?: Date,
        numWinnersRemaining?: number) {
        this.scavengerHuntId = scavengerHuntId;
        this.rewardTypeEnum = rewardTypeEnum;
        this.scavengerHuntDate = scavengerHuntDate;
        this.numWinnersRemaining = numWinnersRemaining;
    }

    
}