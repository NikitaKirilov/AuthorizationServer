import AuthorizationDto from "../types/AuthorizationDto.ts";
import axiosInstance from "../configs/axiosConfig.ts";

const API_URL = "/users/authorizations";

const authorizationApi = {
    getAllUserAuthorizations: async (): Promise<AuthorizationDto[]> => {
        const response = await axiosInstance.get<AuthorizationDto[]>(API_URL, {});

        const data = response.data;

        return data.map((authorization) => ({
            ...authorization,
            createdAt: new Date(authorization.createdAt),
            updatedAt: new Date(authorization.updatedAt),
        }));
    },

    getAllUserAuthorizationsByDeviceId: async (deviceId: string): Promise<AuthorizationDto[]> => {
        const response = await axiosInstance.get<AuthorizationDto[]>(
            `${API_URL}/${deviceId}`, {},
        );

        const data = response.data;

        return data.map((authorization) => ({
            ...authorization,
            createdAt: new Date(authorization.createdAt),
            updatedAt: new Date(authorization.updatedAt),
        }));
    },

    deleteAuthorizationById: async (id: string): Promise<void> => {
        await axiosInstance.delete(`${API_URL}/${id}`, {});
    },
};

export default authorizationApi;
