import axiosInstance from "./axiosConfig.ts";

const redirectUri = import.meta.env.VITE_REDIRECT_URI;
const clientId = import.meta.env.VITE_CLIENT_ID;

const authApi = {
    getTokens: async (code: string) => {
        const body = new URLSearchParams({
            grant_type: "authorization_code",
            code,
            redirect_uri: redirectUri,
            client_id: clientId,
            code_verifier: localStorage.getItem("code_verifier")!,
        });

        return axiosInstance.post("/oauth2/token", body, {
            headers: {
                "Content-type": "application/x-www-form-urlencoded",
            },
        });
    },
};

export default authApi;
