import {ChangeEvent, MouseEvent, useEffect, useState} from "react";
import {RegistrationData} from "../../types/RegistrationData.ts";
import {checkFieldNotEmpty, validateEmail, validatePassword} from "../../utils/validationUtils.ts";
import {RegistrationPageErrorState} from "../../types/RegistrationPageErrorState.ts";
import authApi from "../../api/authApi.ts";
import {isAxiosError} from "axios";
import {ApiError} from "../../types/ApiError.ts";
import FormField from "../../components/FormField/FormField.tsx";
import BirthdayPicker from "../../components/Inputs/BirthdayPicker.tsx";
import Page from "../../components/Page/Page.tsx";
import Form from "../../components/Form/Form.tsx";
import {TextInput} from "../../components/Inputs/TextInput.tsx";
import {Button} from "../../components/Button/Button.tsx";
import styles from "./RegistrationPage.module.css";
import Header from "../../components/Header/Header.tsx";
import Link from "../../components/Link/Link.tsx";

const TITLE = "Registration";

export default function RegistrationPage() {
    useEffect(() => {
        document.title = TITLE;
    }, []);

    const [registrationData, setRegistrationData] = useState<RegistrationData>({
        email: "",
        password: "",
        nickname: "",
        givenName: "",
        familyName: "",
        birthday: null,
    });

    const [error, setError] = useState<RegistrationPageErrorState>({
        emailValidationError: null,
        passwordValidationError: null,
        nicknameValidationError: null,
        givenNameValidationError: null,
        familyNameValidationError: null,
        apiError: null,
    });

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setRegistrationData(prev => ({...prev, [name]: value}));
    };

    const handleSubmit = async (e: MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        const emailError = validateEmail(registrationData.email);
        const passwordError = validatePassword(registrationData.password);
        const nicknameError = checkFieldNotEmpty(registrationData.nickname);
        const familyNameError = checkFieldNotEmpty(registrationData.familyName);
        const givenNameError = checkFieldNotEmpty(registrationData.givenName);

        setError({
            emailValidationError: emailError,
            passwordValidationError: passwordError,
            nicknameValidationError: nicknameError,
            familyNameValidationError: familyNameError,
            givenNameValidationError: givenNameError,
            apiError: null,
        });

        if (emailError || passwordError || nicknameError || familyNameError || givenNameError) {
            return;
        }

        await authApi.register(registrationData)
            .then(response => {
                if (response.status === 200) {
                    globalThis.localStorage.setItem("email", registrationData.email);
                    globalThis.location.href = "/app/registrations/verify";
                }
            })
            .catch(
                (error) => {
                    if (isAxiosError(error) && error.response) {
                        const apiError = error.response.data as ApiError;
                        setError((prev) => ({
                            ...prev,
                            apiError: apiError.message,
                        }));
                    }
                },
            );
    };

    return (
        <Page className={styles.page}>
            <Form className={styles.form} width={"sm"} error={error.apiError}> {/*TODO: error*/}
                <Header title={"Регистрация"} subtitle={"Пожалуйста введите ваши данные"} align={"center"}/>
                <FormField className={styles.field}
                           label={"Почта"}
                           htmlFor={"email"}
                           error={error.emailValidationError}>
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
                           error={error.passwordValidationError}>
                    <TextInput id={"password"}
                               name={"password"}
                               type={"password"}
                               autoComplete={"new-password"}
                               placeholder={"Введите пароль"}
                               value={registrationData.password}
                               onChange={handleChange}/>
                </FormField>
                <FormField className={styles.field}
                           label={"Псевдоним"}
                           htmlFor={"nickname"}
                           error={error.nicknameValidationError}>
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
                           error={error.givenNameValidationError}>
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
                           error={error.familyNameValidationError}>
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
                           htmlFor={"birthday"}>
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
