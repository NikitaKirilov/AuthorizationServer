import axiosInstance from "../configs/axiosConfig.ts";
import {PageResponse} from "../types/PageResponse.ts";
import {UserDto, UserWithRoles} from "../types/UserDto.ts";

const API_URL = "/admin/users";

const userAdminApi = {
    getAllUsers: async (page: number, filters?: string) => {
        const filterParams = Object.fromEntries(
            new URLSearchParams(filters),
        );

        const response = await axiosInstance.get<PageResponse<UserDto>>(
            API_URL, {params: {page, ...filterParams}},
        );

        const data = response.data;

        return {
            ...data,
            content: data.content.map((item) => ({
                ...item,
                birthday: item.birthday,
                createdAt: new Date(item.createdAt),
                updatedAt: new Date(item.createdAt),
            })),
        };
    },

    getUserById: async (id: string) => {
        const response = await axiosInstance.get<UserWithRoles>(
            `${API_URL}/${id}`, {},
        );

        const data = response.data;

        return {
            ...data,
            user: {
                ...data.user,
                birthday: new Date(data.user.birthday),
                createdAt: new Date(data.user.createdAt),
                updatedAt: new Date(data.user.updatedAt),
            },
        };
    },

    updateUserById: async (id: string, dto: UserDto): Promise<UserDto> => {
        const response = await axiosInstance.put<UserDto>(`${API_URL}/${id}`, dto, {
            headers: {
                "Content-Type": "application/json",
            },
        });

        const data = response.data;

        return {
            ...data,
            birthday: new Date(data.birthday),
            createdAt: new Date(data.createdAt),
            updatedAt: new Date(data.updatedAt),
        };
    },

    assignRoles: async (userId: string, roles: string[]) => {
        await axiosInstance.put(`${API_URL}/${userId}/roles`, roles, {
            headers: {
                "Content-Type": "application/json",
            },
        });
    },

    revokeRoles: async (userId: string, roles: string[]) => {
        await axiosInstance.delete(`${API_URL}/${userId}/roles`, {
            data: roles,
            headers: {
                "Content-Type": "application/json",
            },
        });
    },

    deleteUser: async (id: string) => {
        await axiosInstance.delete(`${API_URL}/${id}`, {});
    },
};

export default userAdminApi;
