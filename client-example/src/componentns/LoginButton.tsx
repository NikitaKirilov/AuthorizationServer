import {nanoid} from "nanoid";
import React from "react";
import pkceChallenge from "pkce-challenge";

const authorizationServerUrl = import.meta.env.VITE_AUTHORIZATION_SERVER_URL;
const clientId = import.meta.env.VITE_CLIENT_ID;
const redirectUri = import.meta.env.VITE_REDIRECT_URI;

export default function LoginButton() {
    const handleClick = async (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        const state = nanoid();
        localStorage.setItem("state", state);

        const { code_verifier, code_challenge } = await pkceChallenge(128);
        localStorage.setItem("code_verifier", code_verifier);

        const params = new URLSearchParams({
            response_type: "code",
            state: state,
            client_id: clientId,
            redirect_uri: redirectUri,
            scope: "openid profile email",
            code_challenge: code_challenge,
            code_challenge_method: "S256",
            resource: "resource_server"
        });

        globalThis.location.href = authorizationServerUrl + "/oauth2/authorize?" + params;
    };

    return (
        <button onClick={handleClick}>Login</button>
    );
}
