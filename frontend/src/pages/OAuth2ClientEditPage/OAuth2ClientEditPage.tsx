import React, {useEffect, useState} from "react";
import OAuth2Client from "../../types/OAuth2Client.ts";
import {useNavigate, useSearchParams} from "react-router-dom";
import {TextInput} from "../../components/Inputs/TextInput.tsx";
import Select, {MultiValue, SingleValue} from "react-select";
import CreatableSelect from "react-select/creatable";
import styles from "./OAuth2ClientEditPage.module.css";
import {SelectOption, selectStylesConfig} from "../../configs/selectStylesConfig.ts";
import Switch from "react-switch";
import FormField from "../../components/FormField/FormField.tsx";
import {DurationUnit} from "../../types/Duration.ts";
import {oAuth2ClientApi} from "../../api/oAuth2ClientApi.ts";
import {Button} from "../../components/Button/Button.tsx";
import {ApiError} from "../../types/ApiError.ts";
import {isAxiosError} from "axios";
import {toast, Toaster} from "react-hot-toast";
import {ArrowLeft} from "lucide-react";


const scopeOptions: SelectOption[] = [
    {value: "openid", label: "openid"},
    {value: "profile", label: "profile"},
    {value: "email", label: "email"},
    {value: "authorities", label: "authorities"},
];

const durationOptions: SelectOption[] = Object.values(DurationUnit).map(value => ({
    value: value, label: value,
}));

const defaultFormErrorState = {
    clientId: "",
    clientName: "",
    scopes: "",
    accessTokenTimeToLive: "",
    refreshTokenTimeToLive: "",
    formError: "",
};

const requiredTextFields = new Set(["clientId", "clientName"]);


export default function OAuth2ClientEditPage() {
    const navigate = useNavigate();
    const [params] = useSearchParams();

    const [formErrorState, setFormErrorState] = useState(defaultFormErrorState);
    const [header, setHeader] = useState("Создание клиента");
    const [oAuth2Client, setOAuth2Client] = useState<OAuth2Client>({
        clientId: "",
        clientName: "",
        clientDescription: "",
        scopes: [],
        redirectUris: [],
        postLogoutRedirectUris: [],
        jwkSetUrl: "",
        requireProofKey: true,
        requireAuthorizationConsent: false,
        reuseRefreshTokens: true,
        accessTokenTimeToLive: {
            value: 5,
            unit: DurationUnit.MINUTES,
        },
        refreshTokenTimeToLive: {
            value: 30,
            unit: DurationUnit.DAYS,
        },
    });

    const clientId = params.get("clientId");

    const handleInputChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => {
        const {name, value} = e.target;

        if (requiredTextFields.has(name)) {
            setFormErrorState(prev => ({
                ...prev,
                [name]: value ? "" : `поле не должно быть пустым`,
            }));
        }

        setOAuth2Client(prev => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleDurationValueChange =
        (e: React.ChangeEvent<HTMLInputElement>, field: "accessTokenTimeToLive" | "refreshTokenTimeToLive") => {
            const value = e.target.value;
            setOAuth2Client(prev => ({
                ...prev,
                [field]: {
                    ...prev[field],
                    value: Number(value),
                },
            }));

            setFormErrorState(prev => ({
                ...prev,
                [field]: Number(value) === 0 ? "Время жизни токена должно быть больше 0" : "",
            }));
        };

    const handleDurationUnitChange = (
        option: SingleValue<SelectOption>, field: "accessTokenTimeToLive" | "refreshTokenTimeToLive",
    ) => {
        setOAuth2Client(prev => ({
            ...prev,
            [field]: {
                ...prev[field],
                unit: option?.value,
            },
        }));
    };

    const handleScopesChange = async (
        selected: MultiValue<SelectOption>,
    ) => {
        setOAuth2Client(prev => ({
            ...prev,
            scopes: selected.map(scope => scope.value),
        }));

        if (selected.length === 0) {
            setFormErrorState(prev => ({
                ...prev, scopes: "Поле scopes не должно быть пустым",
            }));
        }
    };

    const handleRedirectURIsChange = async (
        selected: MultiValue<SelectOption>,
    ) => {
        setOAuth2Client(prev => ({
            ...prev,
            redirectUris: selected.map(scope => scope.value),
        }));
    };

    const handlePostLogoutRedirectUrisChange = async (
        selected: MultiValue<SelectOption>,
    ) => {
        setOAuth2Client(prev => ({
            ...prev,
            postLogoutRedirectUris: selected.map(scope => scope.value),
        }));
    };

    const handleSwitchChange = (
        field: keyof OAuth2Client,
    ) =>
        (checked: boolean) => {
            setOAuth2Client(prev => ({
                ...prev,
                [field]: checked,
            }));
        };

    const handleButtonClicked = async (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        try {
            const response = await oAuth2ClientApi.createOrUpdateOAuth2Client(oAuth2Client);
            setOAuth2Client(response);
            toast.success("Клиент успешно сохранён");
        } catch (error) {
            if (!isAxiosError(error)) {
                console.error(error);
                return;
            }

            setFormErrorState(defaultFormErrorState);

            const data = error.response?.data as ApiError;
            if (data?.details) {
                const details = data.details;
                Object.entries(details).forEach(([key, value]) => {
                    setFormErrorState(prev => ({...prev, [key]: value}));
                });
            } else {
                setFormErrorState(prev => ({
                    ...prev,
                    formError: data.message,
                }));
            }
        }
    };

    useEffect(() => {
        if (clientId) {
            setHeader("Редактирование клиента");

            oAuth2ClientApi.getOAuth2Client(clientId)
                .then(data => setOAuth2Client(data));
        }

        document.title = header;
    }, [clientId, header]);

    return (
        <div className={styles.page}>
            <Toaster/>
            <div className={styles.header}>
                <ArrowLeft className={styles.headerBackIcon} size={24} onClick={() => navigate(-1)}/>
                <span>{header}</span>
            </div>
            <div className={styles.form}>
                <div className={styles.info}>
                    <span>Основная информация</span>
                    <div className={styles.row}>
                        <FormField label={"Client ID *"} htmlFor={"clientId"} error={formErrorState.clientId}>
                            <TextInput id={"clientId"}
                                       name={"clientId"}
                                       placeholder={"Введите client id"}
                                       value={oAuth2Client.clientId}
                                       onChange={handleInputChange}/>
                        </FormField>
                        <FormField label={"Client Name *"} htmlFor={"clientName"} error={formErrorState.clientName}>
                            <TextInput id={"clientName"}
                                       name={"clientName"}
                                       placeholder={"Введите client name"}
                                       value={oAuth2Client.clientName}
                                       onChange={handleInputChange}/>
                        </FormField>
                    </div>
                    <FormField label={"Client Description"} htmlFor={"clientDescription"}>
                            <textarea id={"clientDescription"}
                                      className={styles.clientDescription}
                                      name={"clientDescription"}
                                      placeholder={"Введите описание клиента"}
                                      value={oAuth2Client.clientDescription}
                                      rows={3}
                                      onChange={handleInputChange}/>
                    </FormField>
                </div>
                <div className={styles.clientSettings}>
                    <span>Настройки клиента</span>
                    <div className={styles.row}>
                        <FormField label={"Scopes *"} htmlFor={"scopes"} error={formErrorState.scopes}>
                            <Select id={"scopes"}
                                    placeholder={"Выбрать"}
                                    options={scopeOptions}
                                    styles={selectStylesConfig}
                                    className={styles.selectWrapper}
                                    isMulti
                                    value={oAuth2Client.scopes
                                        .map(scope => scopeOptions.find(option => scope === option.value))
                                        .filter((option): option is SelectOption => option !== undefined)}
                                    onChange={handleScopesChange}/>
                        </FormField>
                        <FormField label={"Redirect URIs"} htmlFor={"redirectUris"}>
                            <CreatableSelect id={"redirectUris"}
                                             noOptionsMessage={() => null}
                                             styles={selectStylesConfig}
                                             className={styles.selectWrapper}
                                             isMulti
                                             value={oAuth2Client.redirectUris.map(
                                                 uri => ({value: uri, label: uri}),
                                             )}
                                             onChange={handleRedirectURIsChange}
                                             placeholder={"Ввести"}
                                             formatCreateLabel={(inputValue) => `Добавить "${inputValue}"`}
                            />
                        </FormField>
                    </div>
                    <div className={styles.row}>
                        <FormField label={"Post Logout Redirect URIs"} htmlFor={"postLogoutRedirectUris"}>
                            <CreatableSelect id={"postLogoutRedirectUris"}
                                             styles={selectStylesConfig}
                                             className={styles.selectWrapper}
                                             isMulti
                                             noOptionsMessage={() => null}
                                             onChange={handlePostLogoutRedirectUrisChange}
                                             value={oAuth2Client.postLogoutRedirectUris.map(
                                                 uri => ({value: uri, label: uri}),
                                             )}
                                             placeholder={"Ввести"}
                                             formatCreateLabel={(inputValue) => `Добавить "${inputValue}"`}
                            />
                        </FormField>
                        <FormField label={"JWK Set URL"} htmlFor={"jwkSetUrl"}>
                            <TextInput name={"jwkSetUrl"}
                                       placeholder={"Введите jwk set url"}
                                       value={oAuth2Client.jwkSetUrl}
                                       onChange={handleInputChange}/>
                        </FormField>
                    </div>
                    <div className={styles.switchRow}>
                        <div className={styles.switchWrapper}>
                            <Switch onChange={handleSwitchChange("requireProofKey")}
                                    uncheckedIcon={false}
                                    checkedIcon={false}
                                    onColor={"#6a11cb"}
                                    className={"switch"}
                                    checked={oAuth2Client.requireProofKey}/>
                            <div className={styles.switchInfo}>
                                <span>Require Proof Key</span>
                                <span className={"text-hint"}>Требовать PKCE для авторизации</span>
                            </div>
                        </div>
                        <div className={styles.switchWrapper}>
                            <Switch onChange={handleSwitchChange("requireAuthorizationConsent")}
                                    uncheckedIcon={false}
                                    checkedIcon={false}
                                    onColor={"#6a11cb"}
                                    className={"switch"}
                                    checked={oAuth2Client.requireAuthorizationConsent}/>
                            <div className={styles.switchInfo}>
                                <span>Require Authorization Consent</span>
                                <span className={"text-hint"}>Требовать согласие пользователя</span>
                            </div>
                        </div>
                        <div className={styles.switchWrapper}>
                            <Switch onChange={handleSwitchChange("reuseRefreshTokens")}
                                    uncheckedIcon={false}
                                    checkedIcon={false}
                                    onColor={"#6a11cb"}
                                    checked={oAuth2Client.reuseRefreshTokens}/>
                            <div className={styles.switchInfo}>
                                <span>Reuse Refresh Tokens</span>
                                <span className={"text-hint"}>Переиспользовать refresh токены</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div className={styles.tokenSettings}>
                    <span>Время жизни токенов</span>
                    <div className={styles.row}>
                        <FormField label={"Access Token Time To Live *"} htmlFor={"accessTokenTTL"}
                                   error={formErrorState.accessTokenTimeToLive}>
                            <div className={styles.row}>
                                <TextInput type={"text"}
                                           name={"accessTokenTimeToLive"}
                                           inputMode={"numeric"}
                                           pattern={"[0-9]*"}
                                           placeholder={"Access token TTL"}
                                           value={oAuth2Client.accessTokenTimeToLive.value}
                                           onChange={e => handleDurationValueChange(e, "accessTokenTimeToLive")}/>
                                <Select<SelectOption> name={"accessTokenTimeToLive"}
                                                      className={styles.selectWrapper}
                                                      options={durationOptions} styles={selectStylesConfig}
                                                      value={durationOptions.find(
                                                          option => option.value === oAuth2Client.accessTokenTimeToLive.unit,
                                                      )}
                                                      onChange={(newValue) => handleDurationUnitChange(newValue, "accessTokenTimeToLive")}/>
                            </div>
                        </FormField>
                        <FormField label={"Refresh Token Time To Live *"} htmlFor={"refreshTokenTTL"}
                                   error={formErrorState.refreshTokenTimeToLive}>
                            <div className={styles.row}>
                                <TextInput type={"text"}
                                           inputMode={"numeric"}
                                           pattern={"[0-9]*"}
                                           name={"refreshTokenTimeToLive"}
                                           placeholder={"Access Token TTL"}
                                           value={oAuth2Client.refreshTokenTimeToLive.value}
                                           onChange={e => handleDurationValueChange(e, "refreshTokenTimeToLive")}/>
                                <Select<SelectOption> className={styles.selectWrapper}
                                                      options={durationOptions} styles={selectStylesConfig}
                                                      value={durationOptions.find(
                                                          option => option.value === oAuth2Client.refreshTokenTimeToLive.unit,
                                                      )}
                                                      onChange={(newValue) => handleDurationUnitChange(newValue, "refreshTokenTimeToLive")}

                                />
                            </div>
                        </FormField>
                    </div>
                    <div className={styles.saveButtonWrapper}>
                        <Button className={styles.saveButton} onClick={handleButtonClicked}>
                            Сохранить
                        </Button>
                    </div>
                </div>
            </div>
        </div>
    );
}
