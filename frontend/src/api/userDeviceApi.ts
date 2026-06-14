import axiosInstance from "../configs/axiosConfig.ts";
import {UserDeviceDto} from "../types/UserDeviceDto.ts";

const API_URL = "/users/devices";

const userDeviceApi = {

    getUserDevices: async (page: number = 0) => {
        const response = await axiosInstance.get<UserDeviceDto[]>(
            API_URL, {
                params: {
                    page,
                },
            },
        );
        
        const data = response.data;

        return data.map((device: UserDeviceDto) => ({
            ...device,
            lastLoggedAt: new Date(device.lastLoggedAt),
        }));
    },

    logoutDevice: async (deviceId: string) => {
        await axiosInstance.delete(`${API_URL}/${deviceId}`, {});
    },
};


export default userDeviceApi;
