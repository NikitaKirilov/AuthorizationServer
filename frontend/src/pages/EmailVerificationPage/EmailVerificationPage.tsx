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

    const handleCooldown = (nextCodeAtStr: string) => {
        localStorage.setItem("nextCodeAt", nextCodeAtStr);

        const timer = () => {
            const nextCodeAt = new Date(nextCodeAtStr).getTime();
            const now = Date.now();
            const secondsLeft = Math.max(Math.ceil((nextCodeAt - now) / 1000), 0);

            if (secondsLeft === 0) {
                setError(prev => ({
                    ...prev,
                    emailVerificationCodeCooldownError: null,
                }));
                localStorage.removeItem("nextCodeAt");
            } else {
                setError(prev => ({
                    ...prev,
                    emailVerificationCodeCooldownError: `Next verification code in ${secondsLeft} seconds`,
                }));
                setTimeout(timer, 1000);
            }
        };

        timer();
    };

    useEffect(() => {
        document.title = TITLE;

        const savedNextCodeAt = localStorage.getItem("nextCodeAt");
        if (savedNextCodeAt) {
            handleCooldown(savedNextCodeAt);
        } else {
            authApi.createToken().catch(error => {
                if (isAxiosError(error) && error.response) {
                    const apiError = error.response.data as ApiError;
                    const nextCodeAt = apiError.details?.["next_code_at"] as string | undefined;
                    if (nextCodeAt) handleCooldown(nextCodeAt);
                    else setError(prev => ({
                        ...prev,
                        userError: apiError.message,
                    }));
                }
            });
        }
    }, []);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await authApi.verify(code)
            .then(response => {
                if (response.status === 200) {
                    localStorage.removeItem("email");
                    localStorage.removeItem("nextCodeAt");
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
        await authApi.createToken()
            .then(() => {
                const nextCodeAt = new Date();
                nextCodeAt.setSeconds(nextCodeAt.getSeconds() + 60);
                handleCooldown(nextCodeAt.toString());
            })
            .catch(error => {
                if (isAxiosError(error) && error.response) {
                    const apiError = error.response.data as ApiError;
                    const nextCodeAt = apiError.details?.["next_code_at"] as string | undefined;
                    if (nextCodeAt) handleCooldown(nextCodeAt);
                    else setError(prev => ({
                        ...prev,
                        userError: apiError.message,
                    }));
                }
            });
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
