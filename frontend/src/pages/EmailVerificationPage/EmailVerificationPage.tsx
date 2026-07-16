import OTPInput from "react-otp-input";
import React, {useEffect, useState} from "react";
import styles from "./EmailVerificationPage.module.css";
import authApi from "../../api/authApi.ts";
import {isAxiosError} from "axios";
import {ApiError} from "../../types/ApiError.ts";
import {EmailVerificationPageErrorState} from "../../types/EmailVerificationPageErrorState.ts";
import Page from "../../components/Page/Page.tsx";
import Form from "../../components/Form/Form.tsx";
import {Button} from "../../components/Button/Button.tsx";
import {TextInput} from "../../components/Inputs/TextInput.tsx";
import Header from "../../components/Header/Header.tsx";

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
                emailVerificationCodeCooldownError: `Вы сможете запросить новый код верификации через ${cooldown} секунд`,
            }));
            setTimeout(() => handleTokenRequestCooldown(cooldown - 1), 1000);
        }
    };

    const requestToken = async () => {
        authApi.createNewCode()
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
    }, []);

    const handleSubmit = async (e: React.MouseEvent<HTMLButtonElement>) => {
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
        <Page className={styles.page}>
            <Form className={styles.form}
                  width={"sm"}
                  error={error.userError || error.emailVerificationCodeValidationError}>
                <Header title={"Проверьте ваш email"}
                        subtitle={`Мы отправили код на ваш email${email && ": " + email}`}
                        align={"center"}
                />
                <OTPInput value={code}
                          onChange={setCode}
                          numInputs={6}
                          shouldAutoFocus
                          inputType="text"
                          renderInput={(props) => <TextInput {...props} className={styles.otpInput} style={undefined}/>}
                          containerStyle={styles.otpContainer}
                />
                <Button className={styles.button}
                        disabled={code.length !== 6}
                        onClick={handleSubmit}>
                    Отправить
                </Button>
                <Button className={styles.button}
                        variant={"secondary"}
                        onClick={handleClick}
                        disabled={error.emailVerificationCodeCooldownError !== null}>
                    Запросить новый код
                </Button>
                {error.emailVerificationCodeCooldownError && (
                    <div className={styles.cooldownError}>
                        <span>{error.emailVerificationCodeCooldownError}</span>
                    </div>
                )}
            </Form>
        </Page>
    );
}
