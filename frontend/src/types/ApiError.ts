export interface ApiError {
    status: number,
    message: string,
    details?:  { [key: string]: object };
}
