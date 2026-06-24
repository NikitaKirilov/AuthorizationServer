export interface UserDto {
    id: string;
    email: string;
    emailVerified: boolean;
    nickname: string;
    givenName: string;
    familyName: string;
    birthday: Date;
    blocked: boolean;
    createdAt: Date;
    updatedAt: Date;
}
