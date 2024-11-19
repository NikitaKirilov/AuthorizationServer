import axiosInstance from "./axiosConfig.ts";

const BASE_PATH = "/idp-registrations";

export interface OAuth2LoginInfo {
    clientName: string;
    loginUri: string;
    imageUri: string;
}

export const idpRegistrationApi = {
    getLoginLinks: async (): Promise<Array<OAuth2LoginInfo>> => {
        const response = await axiosInstance.get<Array<OAuth2LoginInfo>>(BASE_PATH + "/dtos");
        return response.data;
    },
};
