import axiosInstance from "./axiosConfig.ts";

export interface LoginRequest {
    email: string;
    password: string;
}

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
