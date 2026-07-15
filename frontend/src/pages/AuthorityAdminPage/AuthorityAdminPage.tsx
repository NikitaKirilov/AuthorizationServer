import Page from "../../components/Page/Page.tsx";
import Header from "../../components/Header/Header.tsx";
import styles from "../TablePage.module.css";
import {Pencil, Search, Trash} from "lucide-react";
import {TextInput} from "../../components/Inputs/TextInput.tsx";
import {useCallback, useEffect, useState} from "react";
import Form from "../../components/Form/Form.tsx";
import GridRow from "../../components/GridRow/GridRow.tsx";
import {PageResponse, pageResponseInitialState} from "../../types/PageResponse.ts";
import {AuthorityDto} from "../../types/AuthorityDto.ts";
import {dateFormatter} from "../../configs/dateFormatterConfig.ts";
import TableFooter from "../../components/PageableInfo/TableFooter.tsx";
import authorityAdminApi from "../../api/authorityAdminApi.ts";
import {Button} from "../../components/Button/Button.tsx";
import {useNavigate} from "react-router-dom";

const title = "Управление привилегиями"

const AuthorityAdminPage = () => {
    const navigate = useNavigate();
    const [search, setSearch] = useState("");
    const [page, setPage] = useState(0);
    const [authorities, setAuthorities] = useState<PageResponse<AuthorityDto>>(pageResponseInitialState);

    const loadAuthorities = useCallback(async () => {
        const authorities = await authorityAdminApi.getAllAuthorities(page, search);
        setAuthorities(authorities);
    }, [page, search]);

    const deleteAuthority  = async (id: string) => {
        if (!globalThis.confirm(`Удалить привилегию с id: ${id}?`)) {
            return;
        }

        await authorityAdminApi.deleteAuthority(id);
        await loadAuthorities();
    }

    useEffect(() => {
        document.title = title;
    }, []);

    useEffect(() => {
        loadAuthorities();
    }, [loadAuthorities]);

    return (
        <Page>
            <Header title={"Управление привилегиями"}
                    subtitle={"Просматривайте и управляйте привилегиями в системе"}/>
            <div className={styles.searchWrapper}>
                <Search className={styles.searchIcon} size={18}/>
                <TextInput className={styles.search}
                           value={search}
                           placeholder={"Введите поисковый запрос"}
                           onChange={e => setSearch(e.target.value)}/>
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
                    authorities.totalElements !== 0 ? authorities.content.map(authority => (
                            <GridRow key={authority.id} className={styles.object}>
                                <div className={styles.field}>
                                    <span className={styles.fieldName}>Название</span>
                                    <span className={styles.fieldValue}>{authority.name}</span>
                                </div>
                                <div className={styles.field}>
                                    <span className={styles.fieldName}>Описание</span>
                                    <span className={styles.fieldValue}>{authority.description}</span>
                                </div>
                                <div className={styles.field}>
                                    <span className={styles.fieldName}>Ресурс</span>
                                    <span className={styles.fieldValue}>{authority.resource}</span>
                                </div>
                                <div className={styles.field}>
                                    <span className={styles.fieldName}>Дата создания</span>
                                    <span className={styles.fieldValue}>{dateFormatter.format(authority.createdAt)}</span>
                                </div>
                                <div className={styles.field}>
                                    <span className={styles.fieldName}>Действия</span>
                                    <div className={styles.actions}>
                                        <Button variant={"icon"} onClick={() => navigate(`authority?id=${authority.id}}`)}>
                                            <Pencil size={18}/>
                                        </Button>
                                        <Button variant={"icon"} onClick={() => deleteAuthority(authority.id)}>
                                            <Trash size={18}/>
                                        </Button>
                                    </div>
                                </div>
                            </GridRow>
                        ),
                    ) : (
                        <div className={styles.emptyContent}>
                            <span>Разрешения не найдены</span>
                        </div>
                    )
                }
                <TableFooter objectName={"привилегий"}
                             onNextPage={() => setPage(prevState => prevState + 1)}
                             onPrevPage={() => setPage(prevState => prevState - 1)}
                             pageable={authorities}
                />
            </Form>
        </Page>
    );
};

export default AuthorityAdminPage;
