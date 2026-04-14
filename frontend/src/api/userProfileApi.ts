import axiosInstance from "./axiosConfig.ts";
import {UpdateUserDto} from "../types/UpdateUserDto.ts";

const userProfileApi = {
    getCurrentUser: async () => {
        return await axiosInstance.get("/users/profile");
    },

    updateUser: async (updateUserDto: UpdateUserDto) => {
        return await axiosInstance.put("/users/profile/update", updateUserDto, {
            headers: {
                "Content-Type": "application/json",
            },
        });
    },
};

export default userProfileApi;
