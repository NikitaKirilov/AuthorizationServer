import OTPInput from "react-otp-input";
import React, {useEffect, useState} from "react";
import "./EmailVerificationPage.css";
import authApi from "../../api/authApi.ts";
import {isAxiosError} from "axios";
import {ApiError} from "../../types/ApiError.ts";
import {EmailVerificationPageErrorState} from "../../types/EmailVerificationPageErrorState.ts";

const TITLE = "Email Verification";

export default function EmailVerificationPage() {
    const email = localStorage.getItem("email");
    const [code, setCode] = useState("");
    const [error, setError] = useState<EmailVerificationPageErrorState>({
        emailVerificationCodeValidationError: null,
        emailVerificationCodeCooldownError: null,
        userError: null,
    });

    const handleTokenRequestCooldown = (cooldown: number) => {
        if (cooldown <= 0) {
            setError(prev => ({
                ...prev,
                emailVerificationCodeCooldownError: null,
            }));
        } else {
            setError(prev => ({
                ...prev,
                emailVerificationCodeCooldownError: `Next verification code in ${cooldown} seconds`,
            }));
            setTimeout(() => handleTokenRequestCooldown(cooldown - 1), 1000);
        }
    };

    const requestToken = async () => {
        authApi.createToken()
            .then(() => {
                setError(() => ({
                    emailVerificationCodeCooldownError: null,
                    emailVerificationCodeValidationError: null,
                    userError: null,
                }));
                handleTokenRequestCooldown(60);
            })
            .catch(error => {
            if (isAxiosError(error) && error.response) {
                const apiError = error.response.data as ApiError;
                const cooldown = apiError.details?.["cooldown"] as number | undefined;
                if (cooldown) handleTokenRequestCooldown(cooldown);
                else setError(prev => ({
                    ...prev,
                    userError: apiError.message,
                }));
            }
            })
    }

    useEffect(() => {
        document.title = TITLE;
        requestToken();
    }, []);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await authApi.verify(code)
            .then(response => {
                if (response.status === 200) {
                    localStorage.removeItem("email");
                    location.href = response.headers["redirect"];
                }
            })
            .catch(error => {
                if (isAxiosError(error) && error.response) {
                    const apiError = error.response.data as ApiError;
                    setError(prev => ({
                        ...prev,
                        emailVerificationCodeValidationError: apiError.message,
                    }));
                }
            });
    };

    const handleClick = async (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        await requestToken();
    };

    return (
        <div className="auth-form">
            <h1>Please check your email</h1>
            <p className="text-hint">We've sent a code to your email{email && ": " + email}</p>
            <form onSubmit={handleSubmit}>
                <OTPInput
                    value={code}
                    onChange={setCode}
                    numInputs={6}
                    shouldAutoFocus
                    inputType="text"
                    renderInput={(props) => <input {...props} />}
                    containerStyle="otp-container"
                    inputStyle="otp-input"
                />
                <button className="default-button" type="submit" disabled={code.length !== 6}>Submit</button>
                {!error.emailVerificationCodeCooldownError &&
                    <button className="text-hint send-new-code-button" onClick={handleClick}>Send new code</button>
                }
                {Object.values(error)
                    .filter(Boolean)
                    .map((errMessage, index) => (
                        <p key={index} className="default-error">{errMessage}</p>
                    ))
                }
            </form>
        </div>
    );
}
