import Page from "../../components/Page/Page.tsx";
import Header from "../../components/Header/Header.tsx";
import {useNavigate, useSearchParams} from "react-router-dom";
import {ChangeEvent, useCallback, useEffect, useState} from "react";
import Form from "../../components/Form/Form.tsx";
import FormField from "../../components/FormField/FormField.tsx";
import {TextInput} from "../../components/Inputs/TextInput.tsx";
import {RoleEditDto, RoleWithAuthoritiesDto} from "../../types/RoleDto.ts";
import roleAdminApi from "../../api/roleAdminApi.ts";
import {PageResponse, pageResponseInitialState} from "../../types/PageResponse.ts";
import {AuthorityDto, authorityToOption} from "../../types/AuthorityDto.ts";
import authorityAdminApi from "../../api/authorityAdminApi.ts";
import {SelectOption, selectStylesConfig} from "../../configs/selectStylesConfig.ts";
import Select from "react-select";
import TextArea from "../../components/Inputs/TextArea/TextArea.tsx";
import {Button} from "../../components/Button/Button.tsx";
import {toast, Toaster} from "react-hot-toast";
import {toApiError} from "../../types/ApiError.ts";
import styles from "./RoleAdminEditPage.module.css";

const roleDetailsInitialState = {
    role: {
        id: "",
        name: "",
        resource: "",
        description: "",
        createdAt: new Date(),
        updatedAt: new Date(),
    },
    authorities: [],
};

const RoleAdminEditPage = () => {
    const navigate = useNavigate();
    const [params] = useSearchParams();
    const [authorities, setAuthorities] = useState<PageResponse<AuthorityDto>>(pageResponseInitialState);
    const [selectedAuthorities, setSelectedAuthorities] = useState<SelectOption[]>([]);
    const [roleDetails, setRoleDetails] = useState<RoleWithAuthoritiesDto>(roleDetailsInitialState);
    const [formError, setFormError] = useState<string | null>(null);

    const id = params.get("id");
    const title = id ? "Редактирование роли" : "Создание роли";

    const loadRole = useCallback(async () => {
        if (!id) {
            return;
        }

        try {
            const role = await roleAdminApi.getRoleById(id);
            if (role) {
                setRoleDetails(role);
                setSelectedAuthorities(role.authorities.map(authorityToOption));
            }
        } catch (error) {
            console.error(error);
        }
    }, [id]);

    useEffect(() => {
        document.title = title;

        authorityAdminApi.getAllAuthorities()
            .then((authorities) => setAuthorities(authorities));

        loadRole();
    }, [title, loadRole]);

    const inputOnChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const {id, value} = e.target;
        setRoleDetails(prevState => {
            if (!prevState) {
                return prevState;
            }

            return {
                ...prevState,
                role: {
                    ...prevState.role,
                    [id]: value,
                },
            };
        });
    };

    const saveButtonOnClick = async () => {
        const role = roleDetails?.role;

        if (!role) {
            return;
        }

        const editDto: RoleEditDto = {
            resource: role.resource,
            name: role.name,
            description: role.description,
            authorityIds: selectedAuthorities?.map(authority => authority.value) ?? [],
        };

        try {
            if (id) {
                const updatedRole = await roleAdminApi.updateRole(id, editDto);
                setRoleDetails(updatedRole);
                setSelectedAuthorities(updatedRole.authorities.map(authorityToOption));
                toast.success("Роль успешно обновлена");
            } else {
                const newRole = await roleAdminApi.createRole(editDto);
                navigate(`?id=${newRole.role.id}`, {replace: true});
                toast.success("Роль успешно добавлена");
            }
            setFormError(null);
        } catch (error) {
            const apiError = toApiError(error);

            if (!apiError) {
                console.error(error);
                return;
            }

            setFormError(apiError.message);
        }
    };

    return (
        <Page className={styles.page}>
            <Toaster/>
            <Header title={title}
                    subtitle={"Просматривайте и управляйте ролями и их разрешениями"}
            />
            <Form className={styles.form} error={formError}>
                <FormField label={"Имя ресурса"} htmlFor={"resource"}>
                    <TextInput id={"resource"}
                               value={roleDetails?.role.resource}
                               onChange={inputOnChange}
                    />
                </FormField>
                <FormField label={"Название роли"} htmlFor={"name"}>
                    <TextInput id={"name"}
                               value={roleDetails?.role.name}
                               onChange={inputOnChange}
                    />
                </FormField>
                <FormField label={"Описание"} htmlFor={"description"}>
                    <TextArea id={"description"}
                              value={roleDetails?.role.description}
                              rows={3}
                              onChange={inputOnChange}
                    />
                </FormField>
                <FormField label={"Разрешения"} htmlFor={"authorities"}>
                    <Select id={"authorities"}
                            placeholder={"Выберите разрешения роли"}
                            noOptionsMessage={() => null}
                            styles={selectStylesConfig}
                            isMulti
                            options={authorities.content.map(authorityToOption)}
                            value={selectedAuthorities}
                            onChange={(value) => setSelectedAuthorities([...value])}
                    />
                </FormField>
                <Button className={styles.saveButton} onClick={saveButtonOnClick}>
                    Сохранить
                </Button>
            </Form>
        </Page>
    );
};

export default RoleAdminEditPage;
