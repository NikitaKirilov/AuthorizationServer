import axiosInstance from "../configs/axiosConfig.ts";
import {PageResponse} from "../types/PageResponse.ts";
import {AuthorityDto, fixDates} from "../types/AuthorityDto.ts";

const API_URL = "/admin/authorities";

const authorityAdminApi = {
    getAuthorityById: async (id: string) => {
        const response = await axiosInstance.get(`${API_URL}/${id}`, {});
        console.log(response);
        return fixDates(response.data);
    },

    getAllAuthorities: async (page?: number, filters?: string) => {
        const filterParams = Object.fromEntries(
            new URLSearchParams(filters),
        );

        const size = page !== undefined ? 5 : undefined;

        const response = await axiosInstance.get<PageResponse<AuthorityDto>>(
            API_URL, {
                params: {
                    page,
                    size,
                    ...filterParams,
                },
            },
        );

        const data = response.data;

        return {
            ...data,
            content: data.content.map(fixDates),
        };
    },

    createAuthority: async (dto: AuthorityDto) => {
        const response = await axiosInstance.post<AuthorityDto>(`${API_URL}`, dto);
        return fixDates(response.data);
    },

    updateAuthority: async (id: string, dto: AuthorityDto) => {
        const response = await axiosInstance.put<AuthorityDto>(`${API_URL}/${id}`, dto);
        return fixDates(response.data);
    },

    deleteAuthority: async (id: string) => {
        await axiosInstance.delete(`${API_URL}/${id}`, {})
    }
};

export default authorityAdminApi;
