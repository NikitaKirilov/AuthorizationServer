import axiosInstance from "../configs/axiosConfig.ts";
import {PageResponse} from "../types/PageResponse.ts";
import {RoleDto, RoleEditDto, RoleWithAuthoritiesDto} from "../types/RoleDto.ts";

const API_URL = "/admin/roles";

const roleAdminApi = {
    getAllRoles: async (page: number, filters?: string) => {
        const filterParams = Object.fromEntries(
            new URLSearchParams(filters),
        );

        const response = await axiosInstance.get<PageResponse<RoleDto>>(
            API_URL, {
                params: {
                    page,
                    size: 5,
                    ...filterParams,
                },
            },
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

    getRoleById: async (id: string) => {
        const response = await axiosInstance.get<RoleWithAuthoritiesDto>(
            `${API_URL}/${id}`, {},
        );

        const data = response.data;

        return {
            ...data,
            role: {
                ...data.role,
                createdAt: new Date(data.role.createdAt),
                updatedAt: new Date(data.role.updatedAt),
            },
        };
    },

    createRole: async (role: RoleEditDto) => {
        const response = await axiosInstance.post<RoleWithAuthoritiesDto>(`${API_URL}`, role);
        return response.data;
    },

    updateRole: async (id: string, role: RoleEditDto) => {
        const response = await axiosInstance.put<RoleWithAuthoritiesDto>(`${API_URL}/${id}`, role);
        return response.data;
    },

    deleteRole: async (roleId: string) => {
        await axiosInstance.delete(`${API_URL}/${roleId}`, {});
    },
};

export default roleAdminApi;
