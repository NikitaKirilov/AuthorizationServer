import {AuthorityDto} from "./AuthorityDto.ts";

export interface RoleDto {
    id: string;

    resource: string;
    name: string;
    description: string;

    createdAt: Date;
    updatedAt: Date;
}

export interface RoleEditDto {
    resource?: string;
    name?: string;
    description?: string;
    authorityIds: string[];
}

export interface RoleWithAuthoritiesDto {
    role: RoleDto;
    authorities: AuthorityDto[];
}

export const roleToOption = (role: RoleDto) => {
    return {
        value: role.id,
        label: `${role.resource}:${role.name}`,
    };
};
