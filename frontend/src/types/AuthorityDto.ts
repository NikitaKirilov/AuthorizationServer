import {RoleDto} from "./RoleDto.ts";

export interface AuthorityDto {
    id: string;

    resource: string;
    name: string;
    description: string;

    createdAt: Date;
    updatedAt: Date;
}

export const authorityDtoInitialState: AuthorityDto = {
    id: "",
    resource: "",
    name: "",
    description: "",
    createdAt: new Date(),
    updatedAt: new Date(),
};

export const authorityToOption = (role: RoleDto) => {
    return {
        value: role.id,
        label: `${role.resource}:${role.name}`,
    };
};

export const fixDates = (dto: AuthorityDto) => {
    return {
        ...dto,
        createdAt: new Date(dto.createdAt),
        updatedAt: new Date(dto.updatedAt),
    };
};
