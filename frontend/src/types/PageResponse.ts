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
