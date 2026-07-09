import {RoleDto} from "./RoleDto.ts";

export interface AuthorityDto {
    id: string;

    resource: string;
    name: string;
    description: string;

    createdAt: Date;
    updatedAt: Date;
}

export const authorityToOption = (role: RoleDto) => {
    return {
        value: role.id,
        label: `${role.resource}:${role.name}`,
    };
};
