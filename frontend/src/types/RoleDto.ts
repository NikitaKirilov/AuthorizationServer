export interface RoleDto {
    id: string;

    resource: string;
    name: string;
    description: string;

    createdAt: Date;
    updatedAt: Date;
}

export const roleToOption = (role: RoleDto) => {
    return {
        value: role.id,
        label: `${role.resource}:${role.name}`,
    };
};
