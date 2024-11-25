import React, {useEffect, useState} from "react";
import authApi from "../../api/authApi.ts";
import SocialLogin from "../../components/SocialLogin/SocialLogin.tsx";
import {LoginRequest} from "../../types/LoginRequest.ts";
import {ResponseError} from "../../types/ResponseError.ts";
import {isAxiosError} from "axios";
import "./LoginPage.css";
import {LoginPageErrorState} from "../../types/LoginPageErrorState.ts";
import {validateEmail, validatePassword} from "../../utils/validationUtils.ts";

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

    const [formData, setFormData] = useState<LoginRequest>({
        username: "",
        password: "",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;

        setFormData((prev) => ({...prev, [name]: value}));
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
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

        await authApi.login(formData).catch(
            (error) => {
                if (isAxiosError(error) && error.response) {
                    const responseError = error.response.data as ResponseError;
                    setError((prev) => ({
                        ...prev,
                        loginError: responseError.message,
                    }));
                }
            },
        );
    };

    return (
        <div className={"login-page"}>
            <h1>LOGIN</h1>
            <p className={"text-grey"}>Please enter your login and password</p>
            <form onSubmit={handleSubmit}>
                <div>
                    <input
                        className={"login-input"}
                        placeholder={"Email"}
                        type="username"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                    />
                    {
                        error.emailValidationError && (
                            <p className={"error"}>{error.emailValidationError}</p>
                        )
                    }
                </div>
                <div>
                    <input
                        className={"login-input password-input"}
                        placeholder={"Password"}
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                    />
                    {
                        error.passwordValidationError && (
                            <p className={"error"}>{error.passwordValidationError}</p>
                        )
                    }
                </div>
                <button className={"login-button"} type="submit">Login</button>
                {
                    error.loginError &&
                    <p className={"error"}>{error.loginError}</p>
                }
            </form>
            <p className={"text-grey reset-password"}>Forgot password?</p>
            <div className={"separator text-grey"}>OR</div>
            <SocialLogin/>
            <p>Don't have an account?</p>
            <a className={"text-grey sign-up"} href="/register">Sign up</a>
        </div>
    );
}
