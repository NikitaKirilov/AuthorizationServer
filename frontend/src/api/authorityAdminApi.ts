import axiosInstance from "../configs/axiosConfig.ts";
import {PageResponse} from "../types/PageResponse.ts";
import {AuthorityDto} from "../types/AuthorityDto.ts";

const API_URL = "/admin/authorities";

const authorityAdminApi = {
    getAllAuthorities: async (page: number, filters?: string) => {
        const filterParams = Object.fromEntries(
            new URLSearchParams(filters),
        );

        const response = await axiosInstance.get<PageResponse<AuthorityDto>>(
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
            content: data.content.map(authority => ({
                ...authority,
                createdAt: new Date(authority.createdAt),
                updatedAt: new Date(authority.updatedAt),
            })),
        };
    },

    deleteAuthority: async (id: string) => {
        await axiosInstance.delete(`${API_URL}/${id}`, {})
    }
};

export default authorityAdminApi;
