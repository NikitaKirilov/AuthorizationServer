import OTPInput from "react-otp-input";
import React, {useEffect, useState} from "react";
import "./EmailVerificationPage.css";
import authApi from "../../api/authApi.ts";
import {isAxiosError} from "axios";
import {ApiError} from "../../types/ApiError.ts";

const TITLE = "Email Verification";

export default function EmailVerificationPage() {
    useEffect(() => {
        document.title = TITLE;
    }, []);

    const [code, setCode] = useState("");
    const [error, setError] = useState<string | undefined>();
    const email = localStorage.getItem("email");

    const handleError = (error: unknown) => {
        if (isAxiosError(error) && error.response) {
            const apiError = error.response.data as ApiError;
            setError(apiError.message);
        }
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        authApi.verify(code)
            .then(response => {
                if (response.status === 200) {
                    globalThis.localStorage.removeItem("email");
                    globalThis.location.href = response.headers["redirect"];
                }
            })
            .catch(error => handleError(error));
    };

    const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        authApi.refresh()
            .catch(error => handleError(error));
    };

    return (
        <div className="auth-form">
            <h1>Please check your email</h1>
            <p className="text-hint">We've sent a code to your email {email && ": " + email}</p>
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
                <p className={"text-hint"}>{}</p>
                <button className={"default-button"} type="submit" disabled={code.length !== 6}>Submit</button>
                <button className={"text-hint send-new-token-button"} onClick={handleClick}>Send new token</button>
                {error && <p className={"default-error"}>{error}</p>}
            </form>
        </div>
    );
}
