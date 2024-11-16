import axios, {AxiosInstance} from "axios";
const baseUrl: string = import.meta.env.VITE_BACKEND_URL;

const axiosInstance: AxiosInstance = axios.create({
    baseURL: baseUrl,
    withCredentials: true
})

export default axiosInstance;
