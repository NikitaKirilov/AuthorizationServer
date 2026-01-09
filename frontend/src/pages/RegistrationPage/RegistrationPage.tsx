import React, {useEffect, useState} from "react";
import {RegistrationData} from "../../types/RegistrationData.ts";
import "./RegistrationPage.css";
import {checkFieldNotEmpty, validateEmail, validatePassword} from "../../utils/validationUtils.ts";
import {RegistrationPageErrorState} from "../../types/RegistrationPageErrorState.ts";
import authApi from "../../api/authApi.ts";
import {isAxiosError} from "axios";
import {ResponseError} from "../../types/ResponseError.ts";
import FormInput from "../../components/FormInput/FormInput.tsx";
import Swal from "sweetalert2";
import "sweetalert2/src/sweetalert2.scss";

const TITLE = "Registration";

export default function RegistrationPage() {

    useEffect(() => {
        document.title = TITLE;
    }, []);

    const [registrationData, setRegistrationData] = useState<RegistrationData>({
        email: "",
        password: "",
        name: "",
        givenName: "",
        familyName: "",
    });

    const [error, setError] = useState<RegistrationPageErrorState>({
        emailValidationError: null,
        passwordValidationError: null,
        nameValidationError: null,
        givenNameValidationError: null,
        familyNameValidationError: null,
        registrationError: null,
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setRegistrationData(prev => ({...prev, [name]: value}));

    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const emailError = validateEmail(registrationData.email);
        const passwordError = validatePassword(registrationData.password);
        const nameError = checkFieldNotEmpty(registrationData.name);
        const familyNameError = checkFieldNotEmpty(registrationData.familyName);
        const givenNameError = checkFieldNotEmpty(registrationData.givenName);

        setError({
            emailValidationError: emailError,
            passwordValidationError: passwordError,
            nameValidationError: nameError,
            familyNameValidationError: familyNameError,
            givenNameValidationError: givenNameError,
            registrationError: null,
        });

        if (emailError || passwordError || nameError || familyNameError || givenNameError) {
            return;
        }

        await authApi.register(registrationData)
            .then(response => {
                if (response.status === 200) {
                    Swal.fire({
                        title: "Success",
                        text: "Email confirmation link was sent to your address",
                        icon: "success",
                        confirmButtonText: "Ok",
                    });
                }
            })
            .catch(
                (error) => {
                    if (isAxiosError(error) && error.response) {
                        const responseError = error.response.data as ResponseError;
                        console.log(responseError);
                        setError((prev) => ({
                            ...prev,
                            registrationError: responseError.message,
                        }));
                    }
                },
            );
    };

    return (
        <div className={"registration-page"}>
            <h1>REGISTRATION</h1>
            <p className={"text-hint"}>Please enter your information</p>
            <form onSubmit={handleSubmit}>
                <FormInput name={"email"} placeholder={"Email"} value={registrationData.email} type={"email"}
                           onChange={handleChange} error={error.emailValidationError}/>
                <FormInput name={"password"} placeholder={"Password"} value={registrationData.password}
                           type={"password"}
                           onChange={handleChange} error={error.passwordValidationError}/>
                <FormInput name={"name"} placeholder={"Name"} value={registrationData.name}
                           onChange={handleChange} error={error.nameValidationError}/>
                <FormInput name={"givenName"} placeholder={"Given name"} value={registrationData.givenName}
                           onChange={handleChange} error={error.givenNameValidationError}/>
                <FormInput name={"familyName"} placeholder={"Family name"} value={registrationData.familyName}
                           onChange={handleChange} error={error.familyNameValidationError}/>
                <button className={"registration-button"} type={"submit"}>Register</button>
            </form>
            <div className={"error"}>{error.registrationError}</div>
            <div className={"separator"}>OR</div>
            <p>Already have an account?</p>
            <a className={"text-hint link"} href="/login">Sign in</a>
        </div>
    );
}
