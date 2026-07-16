import Page from "../../components/Page/Page.tsx";
import {useNavigate, useSearchParams} from "react-router-dom";
import {ChangeEvent, FocusEvent, useCallback, useEffect, useState} from "react";
import {AuthorityDto, authorityDtoInitialState} from "../../types/AuthorityDto.ts";
import authorityAdminApi from "../../api/authorityAdminApi.ts";
import {toApiError} from "../../types/ApiError.ts";
import Header from "../../components/Header/Header.tsx";
import Form from "../../components/Form/Form.tsx";
import FormField from "../../components/FormField/FormField.tsx";
import {TextInput} from "../../components/Inputs/TextInput.tsx";
import TextArea from "../../components/Inputs/TextArea/TextArea.tsx";
import {Button} from "../../components/Button/Button.tsx";
import {toast, Toaster} from "react-hot-toast";
import styles from "./AuthorityAdminEditPage.module.css";
import {checkFieldNotEmpty} from "../../utils/validationUtils.ts";

type AuthorityErrorState = {
    resource?: string,
    name?: string,
    description?: string,
}

const AuthorityAdminEditPage = () => {
    const navigate = useNavigate();
    const [params] = useSearchParams();
    const [formError, setFormError] = useState<string | null>(null);
    const [fieldErrors, setFieldErrors] = useState<AuthorityErrorState>();
    const [authority, setAuthority] = useState<AuthorityDto>(authorityDtoInitialState);

    const id = params.get("id");
    const title = id ? "Редактирование привилегии" : "Создание привилегии";

    const loadAuthority = useCallback(async () => {
        if (!id) {
            return;
        }

        try {
            const authority = await authorityAdminApi.getAuthorityById(id);
            console.log(authority);
            setAuthority(authority);
        } catch (error) {
            const apiError = toApiError(error);
            if (apiError) {
                setFormError(apiError.message);
            }
        }
    }, [id]);

    useEffect(() => {
        document.title = title;
    }, [title]);

    useEffect(() => {
        loadAuthority();
    }, [loadAuthority]);

    const inputOnChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const {id, value} = e.target;
        setAuthority(prevState => {
            if (!prevState) {
                return prevState;
            }

            return {
                ...prevState,
                [id]: value,
            };
        });
    };

    const inputOnBlur = (e: FocusEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const error = checkFieldNotEmpty(e.target.value);
        setFieldErrors(prevState => ({
            ...prevState,
            [e.target.id]: error,
        }));
    };

    const handleSubmit = async () => {
        if (!authority) {
            return;
        }

        try {
            if (id) {
                const updatedAuthority = await authorityAdminApi.updateAuthority(id, authority);
                setAuthority(updatedAuthority);
                toast.success("Привилегия успешно обновлена");
            } else {
                const newAuthority = await authorityAdminApi.createAuthority(authority);
                navigate(`?id=${newAuthority.id}`, {replace: true});
                toast.success("Привилегия успешно создана");
            }
            setFormError(null);
            setFieldErrors({});
        } catch (error) {
            const apiError = toApiError(error);

            if (!apiError) {
                throw error;
            }

            const details = apiError.details;
            if (details) {
                setFieldErrors(details["fields"]);
            }

            setFormError(apiError.message);
        }
    };

    return (
        <Page className={styles.page}>
            <Toaster/>
            <Header title={"Управление привилегией"} subtitle={"Просматривайте поля и управляйте привилегией"}/>
            <Form className={styles.form} error={formError}>
                <FormField label={"Имя ресурса"} htmlFor={"resource"} error={fieldErrors?.resource}>
                    <TextInput id={"resource"}
                               placeholder={"Введите имя ресурса"}
                               value={authority?.resource}
                               onChange={inputOnChange}
                               onBlur={inputOnBlur}
                    />
                </FormField>
                <FormField label={"Имя привилегии"} htmlFor={"name"} error={fieldErrors?.name}>
                    <TextInput id={"name"}
                               placeholder={"Введите имя привилегии"}
                               value={authority?.name}
                               onChange={inputOnChange}
                               onBlur={inputOnBlur}
                    />
                </FormField>
                <FormField label={"Описание"} htmlFor={"description"} error={fieldErrors?.description}>
                    <TextArea id={"description"}
                              placeholder={"Введите описание"}
                              value={authority?.description}
                              onChange={inputOnChange}
                              onBlur={inputOnBlur}
                    />
                </FormField>
                <div className={styles.buttonContainer}>
                    <Button variant={"primary"}
                            onClick={handleSubmit}
                    >
                        Сохранить
                    </Button>
                    <Button variant={"danger"}
                            onClick={() => navigate(-1)}>
                        Отменить
                    </Button>
                </div>
            </Form>
        </Page>
    );
};

export default AuthorityAdminEditPage;
