import styles from "./AuthorizationPage.module.css";
import {Button} from "../../components/Button/Button.tsx";
import {ArrowLeft, Info} from "lucide-react";
import {useCallback, useEffect, useState} from "react";
import {dateFormatter} from "../../configs/dateFormatterConfig.ts";
import {useNavigate, useSearchParams} from "react-router-dom";
import authorizationApi from "../../api/authorizationApi.ts";
import AuthorizationDto from "../../types/AuthorizationDto.ts";


const AuthorizationPage = () => {
    const navigate = useNavigate();
    const [params] = useSearchParams();

    const [authorizations, setAuthorizations] = useState<AuthorizationDto[]>([]);

    const loadAuthorization = useCallback(() => {
        const deviceId = params.get("deviceId");
        if (deviceId) {
            authorizationApi.getAllUserAuthorizationsByDeviceId(deviceId)
                .then(data => setAuthorizations(data));
        } else {
            authorizationApi.getAllUserAuthorizations()
                .then(data => setAuthorizations(data));
        }
    }, [params]);


    const deleteAuthorization = (id: string) => {
        authorizationApi.deleteAuthorizationById(id)
            .then(() => loadAuthorization());
    };

    useEffect(() => {
        loadAuthorization();
    }, [loadAuthorization]);

    return (
        <div className={styles.page}>
            <div className={styles.header}>
                <ArrowLeft className={styles.headerBackIcon} size={24} onClick={() => navigate(-1)}/>
                <div className={styles.headerTextWrapper}>
                    <span>Управление авторизациями</span>
                    <span className={"text-hint"}>
                        Просматривайте и управляйте приложениями, которым вы предоставили доступ к своему аккаунту
                    </span>
                </div>
            </div>
            <div className={styles.form + " form"}>
                <div className={styles.formHeader + " text-hint"}>
                    <span>ID Клиента</span>
                    <span>Клиент</span>
                    <span>Предоставленные права</span>
                    <span>Создано</span>
                    <span>Обновлено</span>
                    <span>Действия</span>
                </div>
                {
                    authorizations.length > 0 ?
                        (
                            <div className={styles.authorizationsList}>
                                {
                                    authorizations.map((authorization: AuthorizationDto) => (
                                        <div key={authorization.id} className={styles.formElement}>
                                            <span>{authorization.oauth2ClientId}</span>
                                            <span>{authorization.clientName}</span>
                                            <span>{authorization.authorizedScopes.join(" ")}</span>
                                            <span>{dateFormatter.format(authorization.createdAt)}</span>
                                            <span>{dateFormatter.format(authorization.updatedAt)}</span>
                                            <div className={styles.authorizationActions}>
                                                <Button variant={"danger"}
                                                        onClick={() => deleteAuthorization(authorization.id)}>
                                                    Отозвать доступ
                                                </Button>
                                            </div>
                                        </div>
                                    ))
                                }
                            </div>
                        ) : (
                            <div className={styles.emptyState}>
                                <span>OAuth2 авторизации не найдены</span>
                            </div>
                        )
                }
                <div className={styles.formFooter}>
                    <Info size={21} className={"text-hint"}/>
                    <span className={"text-hint"}>
                        Отзыв доступа удалит возможность приложения взаимодействовать с вашим аккаунтом
                    </span>
                </div>
            </div>
        </div>
    );
};

export default AuthorizationPage;
