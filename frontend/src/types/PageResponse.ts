export type PageResponse<T> = {
    content: T[],
    totalPages: number,
    totalElements: number,
    numberOfElements: number,
    pageable: {
        offset: number,
        pageNumber: number,
        pageSize: number,
    }
}


export const pageResponseInitialState = {
    content: [],
    totalPages: 0,
    totalElements: 0,
    numberOfElements: 0,
    pageable: {
        pageNumber: 0,
        pageSize: 0,
        offset: 0,
    },
};
