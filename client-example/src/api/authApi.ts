import axiosInstance from "./axiosConfig.ts";
import {nanoid} from "nanoid";
import pkceChallenge from "pkce-challenge";

const authorizationServerUrl = import.meta.env.VITE_AUTHORIZATION_SERVER_URL;
const redirectUri = import.meta.env.VITE_REDIRECT_URI;
const clientId = import.meta.env.VITE_CLIENT_ID;
const clientSecret = import.meta.env.VITE_CLIENT_SECRET;

const defaultScopes = "openid profile email";

const authApi = {
    forceTokenRequestFlow: async () => {
        const state = nanoid();
        localStorage.setItem("state", state);

        const {code_verifier, code_challenge} = await pkceChallenge(128);
        localStorage.setItem("code_verifier", code_verifier);

        const params = new URLSearchParams({
            response_type: "code",
            state: state,
            client_id: clientId,
            redirect_uri: redirectUri,
            scope: defaultScopes,
            code_challenge: code_challenge,
            code_challenge_method: "S256",
            resource: "resource_server",
            prompt: "consent",
        });

        globalThis.location.href = authorizationServerUrl + "/oauth2/authorize?" + params;
    },

    getTokens: async (code: string) => {
        const body = new URLSearchParams({
            grant_type: "authorization_code",
            code,
            redirect_uri: redirectUri,
            client_id: clientId,
            client_secret: clientSecret,
            code_verifier: localStorage.getItem("code_verifier")!,
        });

        return axiosInstance.post("/oauth2/token", body, {
            headers: {
                "Content-type": "application/x-www-form-urlencoded",
            },
        });
    },

    refreshAccessToken: async (refreshToken: string) => {
        const body = new URLSearchParams({
            grant_type: "refresh_token",
            refresh_token: refreshToken,
            redirect_uri: redirectUri,
            client_id: clientId,
            client_secret: clientSecret,
        });

        return axiosInstance.post("/oauth2/token", body, {
            headers: {
                "Content-type": "application/x-www-form-urlencoded",
            },
        });
    },
};

export default authApi;
