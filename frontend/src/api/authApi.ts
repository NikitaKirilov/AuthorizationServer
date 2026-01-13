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

    verify: async (code: string) => {
        return await axiosInstance.put("/registrations/confirm?code=" + code, {});
    },

    refresh: async () => {
        await axiosInstance.post("/registrations/refresh", {});
    },
};

export default authApi;
