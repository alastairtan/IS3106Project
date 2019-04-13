import { StaffTypeEnum } from './StaffTypeEnum.enum';


export class Staff {

    staffId: number;
    firstName: string;
    lastName: string;
    username: string;
    password: string;
    staffTypeEnum: StaffTypeEnum; 
    salt: string;
    profilePicUrl: string;

    constructor(staffId?: number, firstName?: string, lastName?: string, username?: string, password?: string,
        staffTypeEnum?: StaffTypeEnum, profilePicUrl?: string) {

        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.staffTypeEnum = staffTypeEnum;
        this.profilePicUrl = profilePicUrl;

    }
}