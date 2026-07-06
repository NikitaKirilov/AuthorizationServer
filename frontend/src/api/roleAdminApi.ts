import axiosInstance from "../configs/axiosConfig.ts";
import {PageResponse} from "../types/PageResponse.ts";
import {RoleDto} from "../types/RoleDto.ts";

const API_URL = "/admin/roles";

const roleAdminApi = {
    getAllRoles: async (page: number, filters?: string) => {
        const filterParams = Object.fromEntries(
            new URLSearchParams(filters),
        );

        const response = await axiosInstance.get<PageResponse<RoleDto>>(
            API_URL, {params: {page, ...filterParams}},
        );

        const data = response.data;

        return {
            ...data,
            content: data.content.map((item) => ({
                ...item,
                createdAt: new Date(item.createdAt),
                updatedAt: new Date(item.createdAt),
            })),
        };
    },

    deleteRole: async (roleId: string) => {
        await axiosInstance.delete(`${API_URL}/${roleId}`, {});
    },
};

export default roleAdminApi;
