import axiosInstance from "../configs/axiosConfig.ts";
import {RoleDto} from "../types/RoleDto.ts";
import {PageResponse} from "../types/PageResponse.ts";

const API_URL = "/admin/roles";

export const roleApi = {
    getAllRoles: async () => {
        const response = await axiosInstance.get<PageResponse<RoleDto>>(
            API_URL, {},
        );

        return response.data;
    },
};
