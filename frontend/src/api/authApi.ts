import axiosInstance from "./axiosConfig.ts";
import {LoginRequest} from "../types/LoginRequest.ts";

const authApi = {
    login: async (data: LoginRequest) => {
        await axiosInstance.post("/login", data, {
            headers: {
                "content-type": "application/x-www-form-urlencoded",
            },
        });
    },
};

export default authApi;
