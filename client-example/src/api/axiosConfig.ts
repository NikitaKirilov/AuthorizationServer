import axios, {type AxiosInstance} from "axios";

const baseUrl: string = import.meta.env.VITE_AUTHORIZATION_SERVER_URL;

const axiosInstance: AxiosInstance = axios.create({
    baseURL: baseUrl,
    withCredentials: true,
});

export default axiosInstance;
