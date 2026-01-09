import axiosInstance from "./axiosConfig.ts";
import {LoginData} from "../types/LoginData.ts";
import {RegistrationData} from "../types/RegistrationData.ts";

const authApi = {
    login: async (data: LoginData) => {
        return await axiosInstance.post("/login", data, {
            headers: {
                "content-type": "application/x-www-form-urlencoded",
            },
        });
    },

    register: async (data: RegistrationData) => {
        return await axiosInstance.post("/registrations", data, {
            headers: {
                "context-type": "application/json",
            },
        });
    },
};

export default authApi;
