import {isAxiosError} from "axios";

export interface ApiError {
    status: number,
    message: string,
    details?: { [key: string]: object };
}

export const toApiError = (e: unknown) => {
    if (!isAxiosError(e) || !e.response) {
        return null;
    }

    return e.response.data as ApiError;
};
