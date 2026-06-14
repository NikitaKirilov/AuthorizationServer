import {useCallback, useEffect, useState} from "react";
import {UserDeviceDto} from "../../types/UserDeviceDto.ts";
import {Button} from "../../components/Button/Button.tsx";
import styles from "./UserDevicePage.module.css";
import userDeviceApi from "../../api/userDeviceApi.ts";
import {useNavigate} from "react-router-dom";
import {Info} from "lucide-react";
import {dateFormatter} from "../../configs/dateFormatterConfig.ts";

const UserDevicePage = () => {
    const navigate = useNavigate();
    const [devices, setDevices] = useState<UserDeviceDto[]>([]);

    const loadDevices = useCallback(async () => {
        try {
            const data = await userDeviceApi.getUserDevices();
            setDevices(data);
        } catch (e) {
            console.error(e);
        }
    }, []);

    const logoutDevice = async (deviceId: string) => {
        console.log(deviceId);
        await userDeviceApi.logoutDevice(deviceId);
        await loadDevices();
    };

    useEffect(() => {
        loadDevices();
    }, [loadDevices]);

    return (
        <div className={styles.page}>
            <div className={styles.header}>
                <span>Управление устройствами</span>
                <span className={"text-hint"}>Просматривайте и управляйте устройствами</span>
            </div>
            <div className={styles.form + " form"}>
                <div className={styles.formHeader + " text-hint"}>
                    <span>Детали</span>
                    <span>Местоположение</span>
                    <span>Последний вход</span>
                    <span>Текущее</span>
                    <span>Действия</span>
                </div>
                {
                    devices.length === 0 ? (
                        <div className={styles.emptyState}>
                            <span>Устройства не найдены</span>
                        </div>
                    ) : devices.map((device: UserDeviceDto) => (
                        <div key={device.id} className={styles.formElement}>
                            <div className={styles.deviceField}>
                                <span>{device.details}</span>
                            </div>
                            <div className={styles.deviceField}>
                                <span>{device.location}</span>
                            </div>
                            <div className={styles.deviceField}>
                                <span>{dateFormatter.format(device.lastLoggedAt)}</span>
                            </div>
                            <div className={styles.deviceField}>
                                <span>{device.current ? "Да" : "Нет"}</span>
                            </div>
                            <div className={styles.deviceActions}>
                                <Button variant={"secondary"}
                                        onClick={() => navigate(`/app/user/authorizations?deviceId=${device.id}`)}>Управление
                                    авторизациями</Button>
                                <Button variant={"danger"} onClick={() => logoutDevice(device.id)}>Завершить
                                    сессию</Button>
                            </div>
                        </div>
                    ))
                }
                <div className={styles.formFooter}>
                    <Info size={21} className={"text-hint"}/>
                    <span className={"text-hint"}>Если вы не узнаёте устройство, завершите его сессию для защиты аккаунта</span>
                </div>
            </div>
        </div>
    );
};

export default UserDevicePage;
