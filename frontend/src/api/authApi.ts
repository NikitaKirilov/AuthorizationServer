import axiosInstance from "../configs/axiosConfig.ts";
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

    logout: async () => {
        return await axiosInstance.post("/logout", {});
    },

    register: async (data: RegistrationData) => {
        return await axiosInstance.post("/registrations", data, {
            headers: {
                "content-type": "application/json",
            },
        });
    },

    verify: async (token: string) => {
        return await axiosInstance.put("/registrations/verify?sourceCode=" + token, {});
    },

    createNewCode: async () => {
        await axiosInstance.post("/registrations/code/new");
    },

    csrf: async () => {
        await axiosInstance.get("/csrf");
    },

    getCurrentUser: async () => {
        return await axiosInstance.get("/users/profile");
    },
};

export default authApi;
