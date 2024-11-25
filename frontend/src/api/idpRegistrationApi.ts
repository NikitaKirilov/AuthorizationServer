import axiosInstance from "./axiosConfig.ts";
import {ClientRegistrationPublicInfo} from "../types/ClientRegistrationPublicInfo.ts";

const BASE_PATH = "/idp-registrations";

export const idpRegistrationApi = {
    getLoginLinks: async (): Promise<Array<ClientRegistrationPublicInfo>> => {
        const response =
            await axiosInstance.get<Array<ClientRegistrationPublicInfo>>(BASE_PATH + "/infos");

        return response.data;
    },
};
