import Page from "../../components/Page/Page.tsx";
import Header from "../../components/Header/Header.tsx";
import {TextInput} from "../../components/Inputs/TextInput.tsx";
import Form from "../../components/Form/Form.tsx";
import {Eye, Search, Trash} from "lucide-react";
import GridRow from "../../components/GridRow/GridRow.tsx";
import {useCallback, useEffect, useState} from "react";
import {RoleDto} from "../../types/RoleDto.ts";
import {dateFormatter} from "../../configs/dateFormatterConfig.ts";
import {Button} from "../../components/Button/Button.tsx";
import styles from "../TablePage.module.css";
import TableFooter from "../../components/PageableInfo/TableFooter.tsx";
import {PageResponse, pageResponseInitialState} from "../../types/PageResponse.ts";
import roleAdminApi from "../../api/roleAdminApi.ts";
import {useNavigate} from "react-router-dom";

const title = "Управление ролями";

const RoleAdminPage = () => {
    const navigate = useNavigate();
    const [roles, setRoles] = useState<PageResponse<RoleDto>>(pageResponseInitialState);
    const [page, setPage] = useState(0);
    const [search, setSearch] = useState("");

    const loadRoles = useCallback(async () => {
        try {
            const data = await roleAdminApi.getAllRoles(page, search);
            setRoles(data);
        } catch (error) {
            console.log(error);
        }
    }, [page, search]);

    const deleteRole = async (id: string) => {
        if (!globalThis.confirm(`Удалить роль с id: ${id}?`)) {
            return;
        }

        await roleAdminApi.deleteRole(id);
        await loadRoles();
    };

    useEffect(() => {
        document.title = title;

        loadRoles();
    }, [loadRoles]);

    return (
        <Page>
            <Header className={styles.pageHeader}
                    title={"Управление ролями"}
                    subtitle={"Просматривайте и управляйте ролями в системе"}>
            </Header>
            <div className={styles.pageActions}>
                <div className={styles.searchWrapper}>
                    <Search className={styles.searchIcon} size={18}/>
                    <TextInput className={styles.search}
                               value={search}
                               placeholder={"Введите поисковый запрос"}
                               onChange={e => setSearch(e.target.value)}/>
                </div>
                <Button variant={"primary"}
                        onClick={() => navigate("role")}>
                    Создать роль
                </Button>
            </div>
            <Form className={styles.form}>
                <GridRow className={styles.formHeader}>
                    <span>Название</span>
                    <span>Описание</span>
                    <span>Имя ресурса</span>
                    <span>Дата создания</span>
                    <span>Действия</span>
                </GridRow>
                {
                    roles ? (
                        roles.content.map((role: RoleDto) => (
                            <GridRow className={styles.object} key={role.id}>
                                <div className={styles.field}>
                                    <span className={styles.fieldName}>Название</span>
                                    <span>{role.name}</span>
                                </div>
                                <div className={styles.field}>
                                    <span className={styles.fieldName}>Описание</span>
                                    <span className={styles.fieldValue}>{role.description.substring(0, 100)}</span>
                                </div>
                                <div className={styles.field}>
                                    <span className={styles.fieldName}>Имя ресурса</span>
                                    <span>{role.resource}</span>
                                </div>
                                <div className={styles.field}>
                                    <span className={styles.fieldName}>Дата создания</span>
                                    <span className={styles.fieldValue}>{dateFormatter.format(role.createdAt)}</span>
                                </div>
                                <div className={styles.field}>
                                    <span className={styles.fieldName}>Действия</span>
                                    <div className={styles.actions}>
                                        <Button variant={"icon"} onClick={() => navigate(`role?id=${role.id}`)}>
                                            <Eye size={18}/>
                                        </Button>
                                        <Button variant={"icon"} onClick={() => deleteRole(role.id)}>
                                            <Trash size={18}/>
                                        </Button>
                                    </div>
                                </div>
                            </GridRow>
                        ))
                    ) : (
                        <div className={styles.emptyContent}>
                            <span>Роли не найдены</span>
                        </div>
                    )
                }
                <TableFooter objectName={"ролей"}
                             onNextPage={() => setPage(prevState => prevState + 1)}
                             onPrevPage={() => setPage(prevState => prevState - 1)}
                             pageable={roles}/>
            </Form>
        </Page>
    );
};

export default RoleAdminPage;
