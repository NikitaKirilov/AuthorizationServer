import Page from "../../components/Page/Page.tsx";
import Form from "../../components/Form/Form.tsx";
import Header from "../../components/Header/Header.tsx";
import GridRow from "../../components/GridRow/GridRow.tsx";
import {useCallback, useEffect, useState} from "react";
import {UserDto} from "../../types/UserDto.ts";
import {Edit, Search, Trash} from "lucide-react";
import TableFooter from "../../components/PageableInfo/TableFooter.tsx";
import {PageResponse, pageResponseInitialState} from "../../types/PageResponse.ts";
import styles from "./UserAdminPage.module.css";
import userAdminApi from "../../api/userAdminApi.ts";
import {TextInput} from "../../components/Inputs/TextInput.tsx";
import {dateFormatter} from "../../configs/dateFormatterConfig.ts";
import {Button} from "../../components/Button/Button.tsx";
import {useNavigate} from "react-router-dom";
import {isAxiosError} from "axios";
import {ApiError} from "../../types/ApiError.ts";

const UserAdminPage = () => {
    const navigate = useNavigate();
    const [error, setError] = useState<string | null>(null);
    const [page, setPage] = useState(0);
    const [search, setSearch] = useState("");
    const [users, setUsers] = useState<PageResponse<UserDto>>(pageResponseInitialState);

    const loadUsers = useCallback(async () => {
        try {
            const data = await userAdminApi.getAllUsers(page, search);
            setUsers(data);
        } catch (e) {
            console.error(e);
        }
    }, [page, search]);

    const deleteUser = async (id: string) => {
        if (!globalThis.confirm(`Удалить пользователя с id: ${id}?`)) {
            return;
        }

        try {
            await userAdminApi.deleteUser(id);
            await loadUsers();
        } catch (e) {
            if (isAxiosError(e) && e.response) {
                const apiError = e.response.data as ApiError;
                setError(apiError.message);
            }
        }
    };

    useEffect(() => {
        loadUsers();
    }, [loadUsers]);

    return (
        <Page>
            <Header title={"Управление пользователями"}
                    subtitle={"Просматривайте и управляйте пользователями приложения"}
            />
            <div className={styles.searchWrapper}>
                <Search className={styles.searchIcon}/>
                <TextInput className={styles.search}
                           placeholder={"Введите поисковые фильтры"}
                           onChange={e => setSearch(e.target.value)}/>
            </div>
            <Form width={"lg"} className={styles.form} error={error}>
                <GridRow>
                    <span className={"text-hint"}>Пользователь</span>
                    <span className={"text-hint"}>Email</span>
                    <span className={"text-hint"}>Email Verified</span>
                    <span className={"text-hint"}>Статус</span>
                    <span className={"text-hint"}>Дата регистрации</span>
                    <span className={"text-hint"}>Действия</span>
                </GridRow>
                {
                    users.totalElements !== 0 && users.content.map(user => (
                        <GridRow key={user.email}>
                            <span>{user.nickname}</span>
                            <span>{user.email}</span>
                            <span>{user.emailVerified ? "Да" : "Нет"}</span>
                            <span>{user.blocked ? "Заблокирован" : "Активен"}</span>
                            <span>{dateFormatter.format(user.createdAt)}</span>
                            <div className={styles.actions}>
                                <Button variant={"icon"}>
                                    <Edit size={18} onClick={() => navigate(`${user.id}`)}/>
                                </Button>
                                <Button variant={"icon"}>
                                    <Trash size={18} onClick={() => deleteUser(user.id)}/>
                                </Button>
                            </div>
                        </GridRow>
                    ))
                }
                <TableFooter pageable={users}
                             objectName={"пользователей"}
                             notFoundText={"Пользователи не найдены"}
                             onPrevPage={() => setPage(prevState => prevState - 1)}
                             onNextPage={() => setPage(prevState => prevState + 1)}
                />
            </Form>
        </Page>
    );
};


export default UserAdminPage;
