import axiosInstance from "../configs/axiosConfig.ts";
import {RoleDto} from "../types/RoleDto.ts";

const API_URL = "/admin/roles";

export const roleApi = {
    getAllRoles: async () => {
        const response = await axiosInstance.get<RoleDto[]>(
            API_URL, {},
        );

        return response.data;
    },
};
