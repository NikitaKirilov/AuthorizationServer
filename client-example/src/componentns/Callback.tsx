import {useEffect} from "react";
import authApi from "../api/authApi.ts";
import type {TokenResponse} from "../types/TokenResponse.ts";

export default function Callback() {
    useEffect(() => {
        const params = new URLSearchParams(globalThis.location.search);
        const code = params.get("code");

        if (!code) {
            throw new Error("code not found");
        }

        authApi.getTokens(code).then(response => {
            if (response.data) {
                const data = response.data as TokenResponse;

                sessionStorage.setItem("access_token", data.access_token);
                sessionStorage.setItem("id_token", data.access_token);
                sessionStorage.setItem("refresh_token", data.refresh_token);
            }

            globalThis.location.href = "/home";
        });
    }, []);

    return (
        <div>
            <h1>TEST</h1>
        </div>
    );
}

