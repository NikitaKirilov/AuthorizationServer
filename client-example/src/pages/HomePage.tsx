import {jwtDecode} from "jwt-decode";
import authApi from "../api/authApi.ts";
import {isAxiosError} from "axios";
import type {TokenResponse} from "../types/TokenResponse.ts";
import React, {useState} from "react";

export default function HomePage() {
    const [idToken, setIdToken] = useState(sessionStorage.getItem("id_token"));

    if (idToken) {
        const decodedToken = jwtDecode(idToken);
        console.log(decodedToken);
    }

    const handelLogin = async (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        await authApi.forceTokenRequestFlow();
    };

    const handleRefresh = async (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        const refreshToken = sessionStorage.getItem("refresh_token");
        if (refreshToken) {
            authApi.refreshAccessToken(refreshToken)
                .then(response => {
                    const data = response.data as TokenResponse;

                    sessionStorage.setItem("access_token", data.access_token);
                    sessionStorage.setItem("id_token", data.id_token);
                    sessionStorage.setItem("refresh_token", data.refresh_token);

                    setIdToken(data.id_token);
                })
                .catch((error) => {
                    if (isAxiosError(error) && error.response) {
                        console.log(error.response.data);
                    }
                });
        }
    };

    return (
        <div className={"home-page"}>
            {idToken && <p>{idToken}</p>}
            <button onClick={handelLogin}>Login</button>
            <button onClick={handleRefresh}>Refresh Tokens</button>
        </div>
    );
}
