import React, {useEffect, useState} from "react";
import authApi from "../../api/authApi.ts";
import {LoginData} from "../../types/LoginData.ts";
import {ApiError} from "../../types/ApiError.ts";
import {isAxiosError} from "axios";
import {LoginPageErrorState} from "../../types/LoginPageErrorState.ts";
import {validateEmail, validatePassword} from "../../utils/validationUtils.ts";
import SocialLogin from "../../components/SocialLogin/SocialLogin.tsx";
import googleImg from "../../../assets/google.png";
import {Button} from "../../components/Button/Button.tsx";
import Form from "../../components/Form/Form.tsx";
import FormField from "../../components/FormField/FormField.tsx";
import {TextInput} from "../../components/Inputs/TextInput.tsx";
import Page from "../../components/Page/Page.tsx";
import styles from "./LoginPage.module.css";
import Header from "../../components/Header/Header.tsx";
import Link from "../../components/Link/Link.tsx";

const TITLE = "Login";

export default function LoginPage() {
    useEffect(() => {
        document.title = TITLE;
    }, []);

    const [error, setError] = useState<LoginPageErrorState>({
        emailValidationError: null,
        passwordValidationError: null,
        loginError: null,
    });

    const [formData, setFormData] = useState<LoginData>({
        username: "",
        password: "",
    });

    const handleClick = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormData(prev => ({...prev, [name]: value}));
    };

    const handleSubmit = async (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.preventDefault();

        const emailError = validateEmail(formData.username);
        const passwordError = validatePassword(formData.password);

        setError({
            emailValidationError: emailError,
            passwordValidationError: passwordError,
            loginError: null,
        });

        if (emailError || passwordError) {
            return;
        }

        await authApi.login(formData)
            .catch(
                (error) => {
                    if (isAxiosError(error) && error.response) {
                        const apiError = error.response.data as ApiError;
                        setError((prev) => ({
                            ...prev,
                            loginError: apiError.message,
                        }));
                    }
                },
            );
    };

    return (
        <Page className={styles.page}>
            <Form className={styles.form} width={"sm"} error={error.loginError}>
                <Header className={styles.header} align={"center"} title={"Вход"}
                        subtitle={"Введите ваш логин и пароль"}/>
                <FormField className={styles.field}
                           label={"Логин"}
                           htmlFor={"username"}
                           error={error.emailValidationError || ""}>
                    <TextInput id={"username"}
                               name={"username"}
                               autoComplete={"email"}
                               placeholder={"Введите логин"}
                               onChange={handleClick}/>
                </FormField>
                <FormField className={styles.field}
                           label={"Пароль"}
                           htmlFor={"password"}
                           error={error.passwordValidationError || ""}>
                    <TextInput id={"password"}
                               name={"password"}
                               type={"password"}
                               autoComplete={"current-password"}
                               placeholder={"Введите пароль"}
                               onChange={handleClick}/>
                </FormField>
                <Button className={styles.loginButton} variant="primary" onClick={handleSubmit}>Войти</Button>
                <div className={styles.formFooter}>
                    <Link to={"/app/registrations/new"}>Зарегистрироваться</Link>
                    <SocialLogin providerName={"Google"} registrationId={"google"} imageUrl={googleImg}/>
                </div>
            </Form>
        </Page>
    );
}
