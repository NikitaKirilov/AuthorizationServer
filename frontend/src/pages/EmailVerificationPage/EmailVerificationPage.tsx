import OTPInput from "react-otp-input";
import React, {useEffect, useState} from "react";
import "./EmailVerificationPage.css";
import authApi from "../../api/authApi.ts";
import {isAxiosError} from "axios";
import {ApiError} from "../../types/ApiError.ts";

const TITLE = "Email Verification";

export default function EmailVerificationPage() {
    const [code, setCode] = useState("");
    const [error, setError] = useState<string | undefined>();
    const email = localStorage.getItem("email");

    const handleCooldown = (nextTokenAtStr: string) => {
        localStorage.setItem("nextTokenAt", nextTokenAtStr);

        const timer = () => {
            const nextTokenAt = new Date(nextTokenAtStr).getTime();
            const now = Date.now();
            const secondsLeft = Math.max(Math.ceil((nextTokenAt - now) / 1000), 0);

            if (secondsLeft === 0) {
                setError(undefined);
                localStorage.removeItem("nextTokenAt");
            } else {
                setError(`Next verification code in ${secondsLeft} seconds`);
                setTimeout(timer, 1000);
            }
        };

        timer();
    };

    useEffect(() => {
        document.title = TITLE;

        const savedNextTokenAt = localStorage.getItem("nextTokenAt");
        if (savedNextTokenAt) {
            handleCooldown(savedNextTokenAt);
        } else {
            authApi.createToken().catch(error => {
                if (isAxiosError(error) && error.response) {
                    const apiError = error.response.data as ApiError;
                    const nextTokenAt = apiError.details?.["next_token_at"] as string | undefined;
                    if (nextTokenAt) handleCooldown(nextTokenAt);
                    else setError(apiError.message);
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
                    localStorage.removeItem("nextTokenAt");
                    location.href = response.headers["redirect"];
                }
            })
            .catch(error => {
                if (isAxiosError(error) && error.response) {
                    const apiError = error.response.data as ApiError;
                    setError(apiError.message);
                }
            });
    };

    const handleClick = async (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        await authApi.createToken()
            .then(() => {
                const nextTokenAt = new Date();
                nextTokenAt.setSeconds(nextTokenAt.getSeconds() + 60);
                handleCooldown(nextTokenAt.toString());
            })
            .catch(error => {
                if (isAxiosError(error) && error.response) {
                    const apiError = error.response.data as ApiError;
                    const nextTokenAt = apiError.details?.["next_token_at"] as string | undefined;
                    if (nextTokenAt) handleCooldown(nextTokenAt);
                    else setError(apiError.message);
                }
            });
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
                <button className="default-button" type="submit" disabled={code.length !== 6}>Submit</button>
                {!localStorage.getItem("nextTokenAt") &&
                    <button className="text-hint send-new-token-button" onClick={handleClick}>Send new token</button>
                }
                <p className="default-error">{error}</p>
            </form>
        </div>
    );
}
