export type OAuth2ClientDto = {
    clientId: string;
    clientName: string;
    clientDescription: string;
    scopes: string[];
    redirectUris: string[];
    postLogoutRedirectUris: string[];
    jwkSetUrl: string;
    requireProofKey: boolean;
    requireAuthorizationConsent: boolean;
    reuseRefreshTokens: boolean;
    accessTokenTimeToLive: string;
    refreshTokenTimeToLive: string;
    createdAt?: Date;
    updatedAt?: Date;
}
