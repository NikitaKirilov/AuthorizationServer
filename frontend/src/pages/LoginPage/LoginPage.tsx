import React, {useEffect, useState} from "react";
import authApi from "../../api/authApi.ts";
import {LoginData} from "../../types/LoginData.ts";
import {ResponseError} from "../../types/ResponseError.ts";
import {isAxiosError} from "axios";
import "./LoginPage.css";
import {LoginPageErrorState} from "../../types/LoginPageErrorState.ts";
import {validateEmail, validatePassword} from "../../utils/validationUtils.ts";
import FormInput from "../../components/FormInput/FormInput.tsx";
import SocialLogin from "../../components/SocialLogin/SocialLogin.tsx";

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

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;

        setFormData(prev => ({...prev, [name]: value}));
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
            <p className={"text-hint"}>Please enter your login and password</p>
            <form onSubmit={handleSubmit}>
                <FormInput name={"username"} placeholder={"Email"} value={formData.username} onChange={handleChange}
                           error={error.emailValidationError}/>
                <FormInput name={"password"} placeholder={"Password"} value={formData.password} onChange={handleChange}
                           error={error.passwordValidationError}/>
                <button className={"login-button"} type="submit">Login</button>
                {
                    error.loginError &&
                    <p className={"error"}>{error.loginError}</p>
                }
            </form>
            <p className={"text-hint reset-password"}>Forgot password?</p>
            <div className={"separator"}>OR</div>
            <SocialLogin providerName={"Google"} registrationId={"google"} imageUrl={"../../../assets/google.png"}/>
            <p>Don't have an account?</p>
            <a className={"text-hint link"} href="/registrations/new">Sign up</a>
        </div>
    );
}
