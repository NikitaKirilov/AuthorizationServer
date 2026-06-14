import styles from "./Sidebar.module.css";
import {BookUser, Menu, MonitorCog, Server, User, UserRoundKey, Users} from "lucide-react";
import {NavLink} from "react-router-dom";
import {useRef} from "react";


const sidebarConfig = [
    {
        to: "/app/user/profile",
        icon: User,
        title: "Профиль",
        subtitle: "Личная информация",
    },
    {
        to: "/app/user/devices",
        icon: MonitorCog,
        title: "Устройства",
        subtitle: "Управление устройствами",
    },
    {
        to: "/app/user/authorizations",
        icon: BookUser,
        title: "OAuth2 авторизации",
        subtitle: "Управление авторизациями",
    },
    {
        to: "/app/oauth2/clients",
        icon: Server,
        title: "OAuth2 клиенты",
        subtitle: "Управление клиентами",
    },
    {
        to: "/app/users",
        icon: Users,
        title: "Пользователи",
        subtitle: "Управление пользователями",
    },
    {
        to: "/app/roles",
        icon: UserRoundKey,
        title: "Роли",
        subtitle: "Управление ролями и привилегиями",
    },
];


const Sidebar = () => {
    const dialogRef = useRef<HTMLDialogElement>(null);
    const sidebarElements = sidebarConfig.map((item) => {
        const Icon = item.icon;

        return (
            <NavLink
                key={item.to}
                to={item.to}
                className={({isActive}) =>
                    `${styles.sidebarOptionLink} ${
                        isActive ? styles.active : ""
                    }`
                }
            >
                <Icon size={24}/>
                <div className={styles.sidebarOptionTextWrapper}>
                    <span>{item.title}</span>
                    <span className="text-hint">{item.subtitle}</span>
                </div>
            </NavLink>
        );
    });

    const handleMenuClick = () => {
        if (dialogRef.current?.open) {
            dialogRef.current.close();
        } else {
            dialogRef.current?.show();
        }
    };

    return (
        <>
            <div className={styles.sidebarShowIcon} onClick={handleMenuClick}> {/*TODO: replace with button*/}
                <Menu/>
                <dialog className={styles.sidebarDialog} ref={dialogRef}>
                    {sidebarElements}
                </dialog>
            </div>

            <div className={styles.sidebar}>
                {sidebarElements}
            </div>
        </>
    );
};

export default Sidebar;
