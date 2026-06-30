import {ChangeEvent, SyntheticEvent, useCallback, useEffect, useState} from "react";
import {UserWithRoles} from "../../types/UserDto.ts";
import userAdminApi from "../../api/userAdminApi.ts";
import Page from "../../components/Page/Page.tsx";
import Header from "../../components/Header/Header.tsx";
import FormField from "../../components/FormField/FormField.tsx";
import Form from "../../components/Form/Form.tsx";
import {TextInput} from "../../components/Inputs/TextInput.tsx";
import {SelectOption, selectStylesConfig} from "../../configs/selectStylesConfig.ts";
import {RoleDto, roleToOption} from "../../types/RoleDto.ts";
import {roleApi} from "../../api/roleApi.ts";
import Select from "react-select";
import Switch from "react-switch";
import BirthdayPicker from "../../components/Inputs/BirthdayPicker.tsx";
import {Button} from "../../components/Button/Button.tsx";
import styles from "./UserAdminEditPage.module.css";
import {toast, Toaster} from "react-hot-toast";
import {toApiError} from "../../types/ApiError.ts";
import {useParams} from "react-router-dom";

const fieldErrorsInitialState = {
    nickname: null,
    givenName: null,
    familyName: null,
    birthday: null,
};

const TITLE = "Authorization Server - Edit user";

const UserAdminEditPage = () => {
    const {id} = useParams();
    const [userDetails, setUserDetails] = useState<UserWithRoles | null>(null);
    const [selectedRoles, setSelectedRoles] = useState<SelectOption[]>([]);
    const [roles, setRoles] = useState<RoleDto[]>([]);
    const [formError, setFormError] = useState<string | null>(null);
    const [fieldErrors, setFieldErrors] = useState(fieldErrorsInitialState);

    const loadUser = useCallback(async () => {
        if (!id) {
            return;
        }

        try {
            const user = await userAdminApi.getUserById(id);
            if (user) {
                setUserDetails(user);
                setSelectedRoles(user.roles.map(roleToOption));
            }
        } catch (error) {
            console.error(error);
        }
    }, [id]);

    useEffect(() => {
        document.title = TITLE;

        roleApi.getAllRoles()
            .then((roles) => setRoles(roles));

        loadUser();
    }, [loadUser]);

    const textInputOnChange = (e: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;

        setUserDetails(prevState => {
            if (!prevState) {
                return prevState;
            }

            return {
                ...prevState,
                user: {
                    ...prevState.user,
                    [name]: value,
                },
            };
        });
    };

    const birthdayOnChange = (date: Date | null) => {
        if (!date) return;

        setUserDetails(prevState => prevState ?
            {...prevState, user: {...prevState.user, birthday: date}} : prevState,
        );
    };

    const switchOnChange = (
        checked: boolean,
        _event: MouseEvent | SyntheticEvent<MouseEvent | KeyboardEvent, Event>,
        id: string,
    ) => {
        setUserDetails(prevState => {
            if (!prevState) {
                return prevState;
            }

            return {
                ...prevState,
                user: {
                    ...prevState.user,
                    [id]: checked,
                },
            };
        });
    };

    const buttonOnClick = async () => {
        await updateUser();
        await updateRoles();

        toast.success("Пользователь успешно обновлен");

        setFormError(null);
        setFieldErrors(fieldErrorsInitialState);

        await loadUser();
    };

    const updateUser = async () => {
        if (!userDetails) {
            return;
        }

        try {
            const updatedUser = await userAdminApi.updateUserById(userDetails.user.id, userDetails.user);
            setUserDetails(prevState => prevState ? {...prevState, user: updatedUser} : prevState);
        } catch (error) {
            const data = toApiError(error);

            if (!data) {
                throw error;
            }

            setFormError(data.message);

            if (data.details) {
                setFieldErrors({
                    ...fieldErrorsInitialState,
                    ...data.details,
                });
            }

            throw error;
        }
    };

    const updateRoles = async () => {
        if (!userDetails) {
            return;
        }

        const oldIds = new Set(userDetails.roles.map(r => r.id));
        const newIds = new Set(selectedRoles.map(r => r.value));

        const added = [...newIds].filter(id => !oldIds.has(id));
        const removed = [...oldIds].filter(id => !newIds.has(id));

        await Promise.all([
            userAdminApi.assignRoles(userDetails.user.id, added),
            userAdminApi.revokeRoles(userDetails.user.id, removed),
        ]);
    };

    return (
        <Page className={styles.page}>
            <Toaster/>
            <Header className={styles.header}
                    title={"Редактирование пользователя"}
                    subtitle={"Просматривайте и управляйте пользователем и его ролями"}
            />
            <Form onSubmit={buttonOnClick} className={styles.form} error={formError}>
                <FormField label={"Псевдоним"} htmlFor={"nickname"} error={fieldErrors.nickname}>
                    <TextInput id={"nickname"}
                               name={"nickname"}
                               placeholder={"Введите псевдоним"}
                               value={userDetails?.user.nickname}
                               onChange={textInputOnChange}
                    />
                </FormField>
                <FormField label={"Имя"} htmlFor={"givenName"} error={fieldErrors.givenName}>
                    <TextInput id={"givenName"}
                               name={"givenName"}
                               placeholder={"Введите имя"}
                               required
                               value={userDetails?.user.givenName}
                               onChange={textInputOnChange}
                    />
                </FormField>
                <FormField label={"Фамилия"} htmlFor={"familyName"} error={fieldErrors.familyName}>
                    <TextInput id={"familyName"}
                               name={"familyName"}
                               placeholder={"Введите фамилию"}
                               value={userDetails?.user.familyName}
                               onChange={textInputOnChange}
                    />
                </FormField>
                <FormField label={"Дата рождения"} htmlFor={"birthday"} error={fieldErrors.birthday}>
                    <BirthdayPicker className={styles.birthday}
                                    selectedDate={userDetails?.user.birthday}
                                    onChange={birthdayOnChange}/>
                </FormField>
                <FormField label={"Роли"} htmlFor={"roles"}>
                    <Select id={"roles"}
                            name={"roles"}
                            placeholder={"Выберите роли"}
                            noOptionsMessage={() => null}
                            styles={selectStylesConfig}
                            isMulti
                            options={roles.map(roleToOption)}
                            value={selectedRoles}
                            onChange={(value) => setSelectedRoles([...value])}
                    >
                    </Select>
                </FormField>
                <FormField label={"Аккаунт заблокирован"} htmlFor={"blocked"}>
                    <Switch id={"blocked"}
                                name={"blocked"}
                                uncheckedIcon={false}
                                checkedIcon={false}
                                onColor={"#6a11cb"}
                                className={styles.switch}
                                checked={userDetails?.user.blocked ?? false}
                                onChange={switchOnChange}
                    />
                </FormField>
                <Button className={styles.submitButton} onClick={buttonOnClick}>Сохранить</Button>
            </Form>
        </Page>
    );
};

export default UserAdminEditPage;
