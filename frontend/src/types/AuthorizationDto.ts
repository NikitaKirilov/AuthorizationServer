interface AuthorizationDto {
    id: string;
    oauth2ClientId: string;

    clientName: string;
    authorizedScopes: string[];

    createdAt: Date;
    updatedAt: Date;
}

export default AuthorizationDto;
