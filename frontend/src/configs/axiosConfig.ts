import axios, {AxiosInstance} from "axios";
import Cookies from "js-cookie";
import {router} from "../App.tsx";

const baseUrl: string = import.meta.env.VITE_BACKEND_URL;

const axiosInstance: AxiosInstance = axios.create({
    baseURL: baseUrl,
    withCredentials: true,
});

axiosInstance.interceptors.request.use(
    async (request) => {
    try {
        const csrf = Cookies.get("XSRF-TOKEN");

        if (csrf) {
            request.headers["X-XSRF-TOKEN"] = csrf;
        }

        return request;
    } catch (err) {
        throw new Error(`axios# Problem with request during pre-flight phase: ${err}.`);
    }
});

axiosInstance.interceptors.response.use(
    async (response) => {
        const redirect = response.headers["redirect"];

        if (redirect) {
            await router.navigate(redirect, {replace: true});
        }

        return response;
    },
    async (error) => {
        const message = error.response?.data?.message;

        switch (message) {
            case "EMAIL_NOT_VERIFIED":
                await router.navigate("/app/registrations/verify", {replace: true});
                break;

            case "LOGIN_REQUIRED":
                await router.navigate("/app/login", {replace: true});
                break;
        }

        throw error;
    })

export default axiosInstance;
