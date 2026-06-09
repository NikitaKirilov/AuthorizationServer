import {Duration} from "./Duration.ts";

export default interface OAuth2Client {
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
    accessTokenTimeToLive: Duration;
    refreshTokenTimeToLive: Duration;
    createdAt?: Date;
    updatedAt?: Date;
}
