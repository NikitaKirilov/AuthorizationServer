import axiosInstance from "./axiosConfig.ts";
import {UserUpdateDto} from "../types/UserUpdateDto.ts";
import PasswordUpdateDto from "../types/PasswordUpdateDto.ts";

const userProfileApi = {
    getCurrentUser: async () => {
        return await axiosInstance.get("/users/profile");
    },

    updateUser: async (userUpdateDto: UserUpdateDto) => {
        return await axiosInstance.put("/users/profile", userUpdateDto, {
            headers: {
                "Content-Type": "application/json",
            },
        });
    },

    updatePassword: async (passwordUpdateDto: PasswordUpdateDto) => {
        return await axiosInstance.put("/users/profile/password", passwordUpdateDto, {
            headers: {
                "Content-Type": "application/json",
            },
        });
    },
};

export default userProfileApi;
