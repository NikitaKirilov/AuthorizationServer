import axiosInstance from "../configs/axiosConfig.ts";
import {PageResponse} from "../types/PageResponse.ts";
import {AuthorityDto} from "../types/AuthorityDto.ts";

const API_URL = "/admin/authorities";

const authorityAdminApi = {
    getAllAuthorities: async () => {
        const response = await axiosInstance.get<PageResponse<AuthorityDto>>(
            API_URL, {},
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
};

export default authorityAdminApi;
