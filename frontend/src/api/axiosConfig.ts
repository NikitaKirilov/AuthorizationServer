import axios, {AxiosInstance} from "axios";
import Cookies from "js-cookie";

const baseUrl: string = import.meta.env.VITE_BACKEND_URL;

const axiosInstance: AxiosInstance = axios.create({
    baseURL: baseUrl,
    withCredentials: true,
});

axiosInstance.interceptors.request.use(async (request) => {
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
            globalThis.location.href = redirect;
        }
        return response;
    },
    async (error) => {
        const data = error.response.data;
        if (data.message === "EMAIL_NOT_VERIFIED") {
            globalThis.location.href = "/app/registrations/verify";
        } else if (data.message === "LOGIN_REQUIRED") {
            globalThis.location.href = "/app/login";
        }

        throw error;
    },
);


export default axiosInstance;
