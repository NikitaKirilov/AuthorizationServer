export interface UserUpdateDto {
    nickname: string;
    givenName: string;
    familyName: string;
    birthday: Date | null;
}
