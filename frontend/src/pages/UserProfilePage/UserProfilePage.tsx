import React, {ChangeEvent, MouseEvent, useEffect, useState} from "react";
import userProfileApi from "../../api/userProfileApi.ts";
import {UserDto} from "../../types/UserDto.ts";
import styles from "./UserProfilePage.module.css";
import "react-datepicker/dist/react-datepicker.css";
import FormField from "../../components/FormField/FormField.tsx";
import BirthdayPicker from "../../components/Inputs/BirthdayPicker.tsx";
import {TextInput} from "../../components/Inputs/TextInput.tsx";
import {Button} from "../../components/Button/Button.tsx";
import {CalendarSearch, Eye, Info, LockKeyhole, LogOut, User} from "lucide-react";
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
        birthday: null,
    });

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
                console.log(response.data);
                setUser(response.data as UserUpdateDto);
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

    //TODO: decouple this component in future

    return (
        <div className={styles.page}>
            <Toaster/>
            <div className={styles.header}>
                <span className={styles.headerTextPrimary}>Управление аккаунтом</span>
                <span
                    className={styles.headerTextSecondary}>Просматривайте и изменяйте информацию о своем аккаунте</span>
            </div>
            <div className={`${styles.form} ${formError.updateFormError ? styles.error : ""}`}>
                <div className={styles.formHeader}>
                    <div className={styles.formHeaderIconWrapper}>
                        <User height={30} width={30}/>
                    </div>
                    <div className={styles.formHeaderText}>
                        <span className={styles.headerTextPrimary}>Личная информация</span>
                        <span className={styles.headerTextSecondary}>Обновите свои персональные данные</span>
                    </div>
                </div>
                <div className={styles.row}>
                    <FormField label={"Псевдоним"} htmlFor={"nickname"}
                               error={userError.nickname}>
                        <div className={styles.inputWrapper}>
                            <User className={styles.inputIcon} size={25}/>
                            <TextInput className={styles.field}
                                       name={"nickname"}
                                       placeholder={"Введите псевдоним"}
                                       value={user.nickname}
                                       onChange={handleUserChange}>
                            </TextInput>
                        </div>
                    </FormField>
                    <FormField label={"Имя"} htmlFor={"givenName"} error={userError.givenName}>
                        <div className={styles.inputWrapper}>
                            <User className={styles.inputIcon} size={25}/>
                            <TextInput className={styles.field}
                                       name={"givenName"}
                                       placeholder={"Введите имя"}
                                       value={user.givenName}
                                       onChange={handleUserChange}>
                            </TextInput>
                        </div>
                    </FormField>
                </div>
                <div className={styles.row}>
                    <FormField label={"Фамилия"} htmlFor={"familyName"}
                               error={userError.familyName}>
                        <div className={styles.inputWrapper}>
                            <User className={styles.inputIcon}/>
                            <TextInput className={styles.field}
                                       name={"familyName"}
                                       placeholder={"Введите фамилию"}
                                       value={user.familyName}
                                       onChange={handleUserChange}>
                            </TextInput>
                        </div>
                    </FormField>
                    <FormField label={"Дата рождения"} htmlFor={"birthday"}
                               error={userError.birthday}>
                        <div className={styles.inputWrapper}>
                            <CalendarSearch className={styles.inputIcon} size={25}/>
                            <BirthdayPicker className={styles.field}
                                            selectedDate={user.birthday}
                                            onChange={(date) =>
                                                setUser(prev => ({
                                                    ...prev,
                                                    birthday: date ?? prev.birthday,
                                                }))
                                            }
                            />
                        </div>
                    </FormField>
                </div>
                <Button className={styles.formButton} onClick={saveUserButtonOnClick}>
                    Сохранить
                </Button>
                {
                    formError.updateFormError &&
                    <div className={styles.formError}>
                        <Info/>
                        <span>{formError.updateFormError}</span>
                    </div>
                }
            </div>
            <div className={`${styles.form} ${formError.passwordFormError ? styles.error : ""}`}>
                <div className={styles.formHeader}>
                    <div className={styles.formHeaderIconWrapper}>
                        <LockKeyhole height={30} width={30}/>
                    </div>
                    <div className={styles.formHeaderText}>
                        <span className={styles.headerTextPrimary}>Смена пароля</span>
                        <span className={styles.headerTextSecondary}>Обеспечьте безопасность вашего аккаунта</span>
                    </div>
                </div>
                <div className={styles.row}>
                    <FormField label={"Текущий пароль"} htmlFor={"oldPassword"}
                               error={passwordError.oldPassword}>
                        <div className={styles.inputWrapper}>
                            <LockKeyhole className={styles.inputIcon}/>
                            <TextInput className={styles.field}
                                       name={"oldPassword"}
                                       placeholder={"Введите пароль"}
                                       value={passwordUpdateDto.oldPassword}
                                       type={showPassword.oldPassword ? "password" : "text"}
                                       onChange={handlePasswordChange}>
                            </TextInput>
                            <Eye className={styles.showPasswordIcon}
                                 onClick={() => handleShowPasswordClicked("oldPassword")}/>
                        </div>
                        <div className={styles.passwordUpdateHint}>
                            <Info height={15} width={15}/>
                            <span className={styles.headerTextSecondary}>Если вы входили в систему через внешнего поставщика, оставьте поле пустым</span>
                        </div>
                    </FormField>
                    <FormField label={"Новый пароль"} htmlFor={"newPassword"}
                               error={passwordError.newPassword}>
                        <div className={styles.inputWrapper}>
                            <LockKeyhole className={styles.inputIcon}/>
                            <TextInput className={styles.field}
                                       name={"newPassword"}
                                       placeholder={"Введите пароль"}
                                       value={passwordUpdateDto.newPassword}
                                       type={showPassword.newPassword ? "password" : "text"}
                                       onChange={handlePasswordChange}>
                            </TextInput>
                            <Eye className={styles.showPasswordIcon}
                                 onClick={() => handleShowPasswordClicked("newPassword")}/>
                        </div>
                    </FormField>
                </div>
                <Button className={styles.formButton} onClick={savePasswordButtonOnClick}>
                    Изменить пароль
                </Button>
                {
                    formError.passwordFormError &&
                    <div className={styles.formError}>
                        <Info height={20} width={20}/>
                        <span>{formError.passwordFormError}</span>
                    </div>
                }
            </div>
            <div className={styles.form}>
                <div className={styles.row}>
                    <div className={styles.formHeader}>
                        <div className={`${styles.formHeaderIconWrapper} ${styles.logoutIconWrapper}`}>
                            <LogOut height={30} width={30}/>
                        </div>
                        <div className={styles.formHeaderText}>
                            <span className={styles.headerTextPrimary}>Выход из аккаунта</span>
                            <span
                                className={styles.headerTextSecondary}>Завершить текущую сессию и выйти из аккаунта</span>
                        </div>
                    </div>
                    <div className={styles.logoutButtonWrapper}>
                        <Button variant={"danger"} onClick={handleLogout} className={styles.logoutButton}>
                            Выйти из аккаунта
                        </Button>
                    </div>
                </div>
            </div>
        </div>
    );
}
