import { Staff } from './staff';

export class CommunityGoal {
    communityGoalId: number;
    country: string;
    currentPoints: number;
    description: string;
    endDate: Date;
    goalTitle: string;
    startDate: Date;   
    targetPoints: number;  
    rewardPercentage : number;
    completed : boolean;

    staffEntity : Staff;

    constructor(startDate?: Date, endDate?: Date, targetPoints?: number,
        country?: string, goalTitle?: string, description?: string, currentPoints?: number, rewardPercentage? : number, completed? : boolean) {
            
            this.startDate = startDate;
            this.endDate =  endDate;
            this.targetPoints = targetPoints;
            this.country = country;
            this.goalTitle = goalTitle;
            this.description = description;
            this.currentPoints = currentPoints;
            this.rewardPercentage = rewardPercentage;
            this.completed = completed;
    }
}