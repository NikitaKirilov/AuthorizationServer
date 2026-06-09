import axiosInstance from "../configs/axiosConfig.ts";
import OAuth2Client from "../types/OAuth2Client.ts";
import {PageResponse} from "../types/PageResponse.ts";
import {fromIsoDuration, toIsoDuration} from "../types/Duration.ts";
import {OAuth2ClientDto} from "../types/OAuth2ClientDto.ts";


type OAuth2ClientSearchParams = {
    clientId?: string;
    clientName?: string;
    clientDescription?: string;
}


const API_URL = "/oauth2/clients";


export const oAuth2ClientApi = {
    getAllOAuth2Clients: async (page: number = 0, search?: string): Promise<PageResponse<OAuth2Client>> => {
        const searchParams: OAuth2ClientSearchParams = search?.trim()
            ? {
                clientId: search,
                clientName: search,
                clientDescription: search,
            }
            : {};

        const response = await axiosInstance.get<PageResponse<OAuth2ClientDto>>(
            API_URL, {
                params: {
                    page,
                    size: 5,
                    ...searchParams,
                },
            },
        );

        const data = response.data;

        return {
            ...data,
            content: response.data.content.map(dto => ({
                ...dto,
                accessTokenTimeToLive: fromIsoDuration(dto.accessTokenTimeToLive),
                refreshTokenTimeToLive: fromIsoDuration(dto.refreshTokenTimeToLive),
                createdAt: dto.createdAt && new Date(dto.createdAt),
                updatedAt: dto.updatedAt && new Date(dto.updatedAt),
            })),
        };
    },

    getOAuth2Client: async (clientId: string): Promise<OAuth2Client> => {
        const response = await axiosInstance.get<OAuth2ClientDto>(
            `${API_URL}/${clientId}`, {},
        );

        const data = response.data;

        return {
            ...data,
            accessTokenTimeToLive: fromIsoDuration(data.accessTokenTimeToLive),
            refreshTokenTimeToLive: fromIsoDuration(data.refreshTokenTimeToLive),
            createdAt: data.createdAt && new Date(data.createdAt),
            updatedAt: data.updatedAt && new Date(data.updatedAt),
        };
    },

    generateSecret: async (clientId: string): Promise<string> => {
        const response = await axiosInstance.put<string>(
            `${API_URL}/secret/${clientId}`, {},
        );

        return response.data;
    },

    createOrUpdateOAuth2Client: async (dto: OAuth2Client): Promise<OAuth2Client> => {
        const body: OAuth2ClientDto = ({
            ...dto,
            accessTokenTimeToLive: toIsoDuration(dto.accessTokenTimeToLive),
            refreshTokenTimeToLive: toIsoDuration(dto.refreshTokenTimeToLive),
        });

        const response = await axiosInstance.post<OAuth2ClientDto>(
            `${API_URL}`, body, {
                headers: {
                    "Content-Type": "application/json",
                },
            },
        );

        const data = response.data;

        return {
            ...data,
            accessTokenTimeToLive: fromIsoDuration(data.accessTokenTimeToLive),
            refreshTokenTimeToLive: fromIsoDuration(data.refreshTokenTimeToLive),
        };
    },

    deleteOAuth2Client: async (clientId: string) => {
        return await axiosInstance.delete(`${API_URL}/${clientId}`, {});
    },
};
