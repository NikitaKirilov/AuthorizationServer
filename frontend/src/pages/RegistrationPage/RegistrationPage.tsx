import {ChangeEvent, MouseEvent, useEffect, useState} from "react";
import {RegistrationData} from "../../types/RegistrationData.ts";
import authApi from "../../api/authApi.ts";
import {toApiError} from "../../types/ApiError.ts";
import FormField from "../../components/FormField/FormField.tsx";
import BirthdayPicker from "../../components/Inputs/BirthdayPicker.tsx";
import Page from "../../components/Page/Page.tsx";
import Form from "../../components/Form/Form.tsx";
import {TextInput} from "../../components/Inputs/TextInput.tsx";
import {Button} from "../../components/Button/Button.tsx";
import styles from "./RegistrationPage.module.css";
import Header from "../../components/Header/Header.tsx";
import Link from "../../components/Link/Link.tsx";
import {useNavigate} from "react-router-dom";
import {checkFieldNotEmpty, validateEmail, validatePassword} from "../../utils/validationUtils.ts";

const title = "Registration";

const registrationDataInitialState = {
    email: "",
    password: "",
    nickname: "",
    givenName: "",
    familyName: "",
    birthday: null,
};

const fieldErrorsInitialState = {
    email: null,
    password: null,
    nickname: null,
    givenName: null,
    familyName: null,
    birthday: null,
};

export default function RegistrationPage() {
    const navigate = useNavigate();
    const [registrationData, setRegistrationData] = useState<RegistrationData>(registrationDataInitialState);
    const [fieldErrors, setFieldErrors] = useState<Record<string, string | null>>(fieldErrorsInitialState);
    const [formError, setFormError] = useState<string | null>(null);

    useEffect(() => {
        document.title = title;
    }, []);

    const registerUser = async () => {
        try {
            await authApi.register(registrationData);
            globalThis.localStorage.setItem("email", registrationData.email);
            navigate("/app/registrations/verify", {replace: true});
        } catch (error) {
            const data = toApiError(error);

            if (!data) {
                throw error;
            }

            setFormError(data.message);

            if (data.details) {
                const fieldErrors = data.details["fields"] as Record<string, string | null>;
                setFieldErrors({
                    ...fieldErrorsInitialState,
                    ...fieldErrors,
                });
            }

            throw error;
        }
    }

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        const fieldError = checkFieldNotEmpty(value);
        setFieldErrors(prevState => ({...prevState, [name]: fieldError}));
        setRegistrationData(prev => ({...prev, [name]: value}));
    };

    const handleSubmit = async (e: MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        const emailError = validateEmail(registrationData.email);
        const passwordError = validatePassword(registrationData.password);
        const nicknameError = checkFieldNotEmpty(registrationData.nickname);
        const familyNameError = checkFieldNotEmpty(registrationData.familyName);
        const givenNameError = checkFieldNotEmpty(registrationData.givenName);

        setFieldErrors({
            email: emailError,
            password: passwordError,
            nickname: nicknameError,
            familyName: familyNameError,
            givenName: givenNameError,
        });

        if (Object.values(fieldErrors).some(value => value !== null)) {
            return;
        }

        await registerUser();
    };

    return (
        <Page className={styles.page}>
            <Form className={styles.form} width={"sm"} error={formError}>
                <Header title={"Регистрация"} subtitle={"Пожалуйста введите ваши данные"} align={"center"}/>
                <FormField className={styles.field}
                           label={"Почта"}
                           htmlFor={"email"}
                           error={fieldErrors.email}>
                    <TextInput id={"email"}
                               name={"email"}
                               autoComplete={"email"}
                               placeholder={"Введите почту"}
                               value={registrationData.email}
                               onChange={handleChange}/>
                </FormField>
                <FormField className={styles.field}
                           label={"Пароль"}
                           htmlFor={"password"}
                           error={fieldErrors.password}>
                    <TextInput id={"password"}
                               name={"password"}
                               autoComplete={"new-password"}
                               placeholder={"Введите пароль"}
                               value={registrationData.password}
                               onChange={handleChange}/>
                </FormField>
                <FormField className={styles.field}
                           label={"Псевдоним"}
                           htmlFor={"nickname"}
                           error={fieldErrors.nickname}>
                    <TextInput id={"nickname"}
                               name={"nickname"}
                               autoComplete={"name"}
                               placeholder={"Введите псевдоним"}
                               value={registrationData.nickname}
                               onChange={handleChange}
                    />
                </FormField>
                <FormField className={styles.field}
                           label={"Имя"}
                           htmlFor={"givenName"}
                           error={fieldErrors.givenName}>
                    <TextInput id={"givenName"}
                               name={"givenName"}
                               autoComplete={"given-name"}
                               placeholder={"Введите имя"}
                               value={registrationData.givenName}
                               onChange={handleChange}
                    />
                </FormField>
                <FormField className={styles.field}
                           label={"Фамилия"}
                           htmlFor={"familyName"}
                           error={fieldErrors.familyName}>
                    <TextInput id={"familyName"}
                               name={"familyName"}
                               autoComplete={"family-name"}
                               placeholder={"Введите фамилию"}
                               value={registrationData.familyName}
                               onChange={handleChange}
                    />
                </FormField>
                <FormField className={styles.field}
                           label={"Дата рождения"}
                           htmlFor={"birthday"}
                           error={fieldErrors.birthday}>
                    <BirthdayPicker className={styles.datepicker}
                                    selectedDate={registrationData.birthday}
                                    onChange={e => {
                                        setRegistrationData(prev => ({
                                            ...prev,
                                            birthday: e,
                                        }));
                                    }}
                    />
                </FormField>
                <Button onClick={handleSubmit}
                        className={styles.registrationButton}
                        variant={"primary"}>
                    Зарегистрироваться
                </Button>
                <div className={styles.formFooter}>
                    <p>У вас уже есть аккаунт?</p>
                    <Link to={"/app/login"}>Войти в систему</Link>
                </div>
            </Form>
        </Page>
    );
}
