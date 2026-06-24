import axiosInstance from "../configs/axiosConfig.ts";
import {PageResponse} from "../types/PageResponse.ts";
import {UserDto} from "../types/UserDto.ts";

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
                birthdate: item.birthday,
                createdAt: new Date(item.createdAt),
                updatedAt: new Date(item.createdAt),
            })),
        };
    },

    deleteUser: async (id: string) => {
        await axiosInstance.delete(`${API_URL}/${id}`, {});
    },
};

export default userAdminApi;
