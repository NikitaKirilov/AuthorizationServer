import React, {ChangeEvent, MouseEvent, useEffect, useState} from "react";
import userProfileApi from "../../api/userProfileApi.ts";
import {UserDto} from "../../types/UserDto.ts";
import "./UserProfilePage.css";
import "react-datepicker/dist/react-datepicker.css";
import FormField from "../../components/FormField/FormField.tsx";
import BirthdayPicker from "../../components/BirthdayPicker/BirthdayPicker.tsx";
import TextInput from "../../components/TextInput/TextInput.tsx";
import PrimaryButton from "../../components/PrimaryButton/PrimaryButton.tsx";
import {Eye, Info, LockKeyhole, LogOut, User} from "lucide-react";
import PasswordUpdateDto from "../../types/PasswordUpdateDto.ts";
import {ApiError} from "../../types/ApiError.ts";
import {UserUpdateDto} from "../../types/UserUpdateDto.ts";
import {toast, Toaster} from "react-hot-toast";
import authApi from "../../api/authApi.ts";


type HidePasswordState = {
    oldPassword: boolean;
    newPassword: boolean;
}


type PasswordUpdateErrorState = {
    oldPassword: string,
    newPassword: string,
}


type UpdateUserErrorState = {
    nickname: string,
    givenName: string,
    familyName: string,
    birthday: string,
}


type FormErrorState = {
    updateFormError: string,
    passwordFormError: string,
}


const TITLE = "Profile";

export function UserProfilePage() {
    const [user, setUser] = useState<UserUpdateDto>({
            nickname: "",
            givenName: "",
            familyName: "",
            birthday: new Date(),
        },
    );

    const userUpdateErrorInitialState = ({
        nickname: "",
        givenName: "",
        familyName: "",
        birthday: "",
    });

    const passwordUpdateErrorInitialState = ({
        oldPassword: "",
        newPassword: "",
    });

    const formErrorInitialState = ({
        updateFormError: "",
        passwordFormError: "",
    });

    const [userError, setUserError] = useState<UpdateUserErrorState>(userUpdateErrorInitialState);
    const [passwordError, setPasswordError] = useState<PasswordUpdateErrorState>(passwordUpdateErrorInitialState);
    const [formError, setFormError] = useState<FormErrorState>(formErrorInitialState);

    const [passwordUpdateDto, setPasswordUpdateDto] = useState<PasswordUpdateDto>({
        oldPassword: "",
        newPassword: "",
    });

    const [showPassword, setShowPassword] = useState<HidePasswordState>({
        oldPassword: true,
        newPassword: true,
    });

    useEffect(() => {
        document.title = TITLE;
        userProfileApi.getCurrentUser()
            .then(response => {
                setUser(response.data as UserDto);
            });
    }, []);

    const handleUserChange = (e: ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setUser(prev => ({...prev, [name]: value}));
    };

    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setPasswordUpdateDto(prev => ({...prev, [name]: value}));
    };

    const handleShowPasswordClicked = (name: keyof typeof showPassword) => {
        setShowPassword(prev => ({...prev, [name]: !prev[name]}));
    };

    const handleLogout = () => {
        authApi.logout().then(r => console.log(r));
    };

    const saveUserButtonOnClick = (e: MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        if (!user) {
            return;
        }

        userProfileApi.updateUser(user)
            .then(response => {
                setUser(response.data as UserDto);
                setUserError(userUpdateErrorInitialState);
                toast.success("Профиль успешно обновлен");
            })
            .catch(error => {
                const data = error.response.data as ApiError;
                if (data?.details) {
                    const details = data.details;
                    Object.entries(details).forEach(([key, value]) => {
                        setUserError(prev => ({...prev, [key]: value}));
                    });
                    setFormError(prev => ({...prev, updateFormError: ""}));
                } else {
                    setFormError(prev => ({...prev, updateFormError: data.message}));
                    setUserError(userUpdateErrorInitialState);
                }
            });
    };

    const savePasswordButtonOnClick = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        userProfileApi.updatePassword(passwordUpdateDto)
            .then(() => {
                setFormError(prev => ({...prev, passwordFormError: ""}));
                setPasswordError(passwordUpdateErrorInitialState);
                toast.success("Пароль успешно обновлен");
            })
            .catch(error => {
                const data = error.response.data as ApiError;
                if (data?.details) {
                    const details = data.details;
                    Object.entries(details).forEach(([key, value]) => {
                        setPasswordError(prev => ({...prev, [key]: value}));
                    });
                    setFormError(prev => ({...prev, passwordFormError: ""}));
                } else {
                    setFormError(prev => ({...prev, passwordFormError: data.message}));
                    setPasswordError(passwordUpdateErrorInitialState);
                }
            });
    };

    return (
        <div className={"profile-page"}>
            <Toaster/>
            <div className={"header"}>
                <span className={"header-text-primary"}>Управление аккаунтом</span>
                <span className={"header-text-secondary"}>Просматривайте и изменяйте информацию о своем аккаунте</span>
            </div>
            <div className={`form ${formError.updateFormError ? "error" : ""}`}>
                <div className={"form-header"}>
                    <div className={"form-header-icon-wrapper"}>
                        <User height={30} width={30}/>
                    </div>
                    <div className={"form-header-text"}>
                        <span className={"header-text-primary"}>Личная информация</span>
                        <span className={"header-text-secondary"}>Обновите свои персональные данные</span>
                    </div>
                </div>
                <div className={"form-row"}>
                    <FormField label={"Псевдоним"} htmlFor={"nickname"}
                               error={userError.nickname}>
                        <TextInput
                            name={"nickname"}
                            placeholder={"Введите псевдоним"}
                            value={user.nickname}
                            onChange={handleUserChange}>
                            <User className={"text-input-icon"}/>
                        </TextInput>
                    </FormField>
                    <FormField label={"Имя"} htmlFor={"givenName"} error={userError.givenName}>
                        <TextInput
                            name={"givenName"}
                            placeholder={"Введите имя"}
                            value={user.givenName}
                            onChange={handleUserChange}>
                            <User className={"text-input-icon"}/>
                        </TextInput>
                    </FormField>
                </div>
                <div className={"form-row"}>
                    <FormField label={"Фамилия"} htmlFor={"familyName"}
                               error={userError.familyName}>
                        <TextInput
                            name={"familyName"}
                            placeholder={"Введите фамилию"}
                            value={user.familyName}
                            onChange={handleUserChange}>
                            <User className={"text-input-icon"}/>
                        </TextInput>
                    </FormField>
                    <FormField label={"Дата рождения"} htmlFor={"birthday"}
                               error={userError.birthday}>
                        <BirthdayPicker
                            selectedDate={user.birthday}
                            onChange={(date) =>
                                setUser(prev => ({
                                    ...prev,
                                    birthday: date ?? prev.birthday,
                                }))
                            }
                        />
                    </FormField>
                </div>
                <PrimaryButton onClick={saveUserButtonOnClick}>
                    Сохранить
                </PrimaryButton>
                {
                    formError.updateFormError &&
                    <div className={"form-error"}>
                        <Info/>
                        <span>{formError.updateFormError}</span>
                    </div>
                }
            </div>
            <div className={`form ${formError.passwordFormError ? "error" : ""}`}>
                <div className={"form-header"}>
                    <div className={"form-header-icon-wrapper"}>
                        <LockKeyhole height={30} width={30}/>
                    </div>
                    <div className={"form-header-text"}>
                        <span className={"header-text-primary"}>Смена пароля</span>
                        <span className={"header-text-secondary"}>Обеспечьте безопасность вашего аккаунта</span>
                    </div>
                </div>
                <div className={"form-row"}>
                    <FormField label={"Текущий пароль"} htmlFor={"oldPassword"}
                               error={passwordError.oldPassword}>
                        <TextInput name={"oldPassword"} placeholder={"Введите пароль"}
                                   value={passwordUpdateDto.oldPassword}
                                   password={showPassword.oldPassword}
                                   onChange={handlePasswordChange}>
                            <LockKeyhole className={"text-input-icon"}/>
                            <Eye onClick={() => handleShowPasswordClicked("oldPassword")}
                                 className={"show-password-icon"}/>
                        </TextInput>
                        <div className={"password-update-hint"}>
                            <Info height={15} width={15}/>
                            <span className={"header-text-secondary"}>Если вы входили в систему через внешнего поставщика, оставьте поле пустым</span>
                        </div>
                    </FormField>
                    <FormField label={"Новый пароль"} htmlFor={"newPassword"}
                               error={passwordError.newPassword}>
                        <TextInput name={"newPassword"} placeholder={"Введите пароль"}
                                   value={passwordUpdateDto.newPassword}
                                   password={showPassword.newPassword}
                                   onChange={handlePasswordChange}>
                            <LockKeyhole className={"text-input-icon"}/>
                            <Eye onClick={() => handleShowPasswordClicked("newPassword")}
                                 className={"show-password-icon"}/>
                        </TextInput>
                    </FormField>
                </div>
                <PrimaryButton onClick={savePasswordButtonOnClick}>
                    Изменить пароль
                </PrimaryButton>
                {
                    formError.passwordFormError &&
                    <div className={"form-error"}>
                        <Info height={20} width={20}/>
                        <span>{formError.passwordFormError}</span>
                    </div>
                }
            </div>
            <div className={"form"}>
                <div className={"form-row"}>
                    <div className={"form-header"}>
                        <div className={"form-header-icon-wrapper red"}>
                            <LogOut height={30} width={30}/>
                        </div>
                        <div className={"form-header-text"}>
                            <span className={"header-text-primary"}>Выход из аккаунта</span>
                            <span
                                className={"header-text-secondary"}>Завершить текущую сессию и выйти из аккаунта</span>
                        </div>
                    </div>
                    <div className={"logout-button-wrapper"}>
                        <button onClick={handleLogout} className={"logout-button"}>
                            Выйти из аккаунта
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
