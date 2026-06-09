import {useCallback, useEffect, useRef, useState} from "react";
import {BookKey, ChevronLeft, ChevronRight, Copy, KeyRound, Pencil, Search, Trash2, TriangleAlert} from "lucide-react";
import {Button} from "../../components/Button/Button.tsx";
import styles from "./OAuth2ClientsPage.module.css";
import OAuth2Client from "../../types/OAuth2Client.ts";
import {oAuth2ClientApi} from "../../api/oAuth2ClientApi.ts";
import {PageResponse} from "../../types/PageResponse.ts";
import {TextInput} from "../../components/Inputs/TextInput.tsx";
import {useNavigate} from "react-router-dom";
import {dateFormatter} from "../../configs/dateFormatterConfig.ts";


export default function OAuth2ClientsPage() {
    const navigate = useNavigate();
    const modalRef = useRef<HTMLDialogElement>(null);

    const [pageResponse, setPageResponse] = useState<PageResponse<OAuth2Client>>({
        content: [],
        totalPages: 0,
        totalElements: 0,
        numberOfElements: 0,
        pageable: {
            pageNumber: 0,
            pageSize: 0,
            offset: 0,
        },
    });

    const [search, setSearch] = useState("");
    const [page, setPage] = useState(0);
    const [clientSecret, setClientSecret] = useState<string | null>(null);
    const [currentClientId, setCurrentClientId] = useState<string | null>(null);

    const loadClients = useCallback(async () => {
        try {
            const data = await oAuth2ClientApi.getAllOAuth2Clients(page, search);
            setPageResponse(data);

            if (data.totalPages <= page) {
                setPage(0);
            }
        } catch (e) {
            console.error(e);
        }
    }, [page, search]);

    useEffect(() => {
        loadClients();
    }, [loadClients]);

    const deleteClient = async (clientId: string) => {
        if (!globalThis.confirm("Удалить OAuth2 клиента?")) {
            return;
        }

        try {
            await oAuth2ClientApi.deleteOAuth2Client(clientId);
            await loadClients();
        } catch (e) {
            console.error(e);
        }
    };

    const copyText = async (text: string) => {
        await navigator.clipboard.writeText(text);
    };

    const nextPage = async () => {
        if (page + 1 < pageResponse.totalPages) setPage(prev => prev + 1);
    };

    const prevPage = async () => {
        if (page != 0) setPage(prev => prev - 1);
    };

    const generateSecret = async () => {
        if (!currentClientId) {
            return;
        }

        const secret = await oAuth2ClientApi.generateSecret(currentClientId);
        setClientSecret(secret);
    };

    const showModal = async (clientId: string) => {
        modalRef.current?.showModal();
        setCurrentClientId(clientId);
    };

    return (
        <div className={styles.page}>
            <dialog className={styles.modalWindow} ref={modalRef}
                    onClose={() => {
                        setCurrentClientId(null);
                        setClientSecret(null);
                    }}>
                <div className={styles.modalHeader}>
                    <div className={styles.modalHeaderIconWrapper}>
                        <KeyRound className={styles.modalHeaderIcon} size={40}/>
                    </div>
                    <div className={styles.headerTextWrapper}>
                        <span>Генерация Client Secret</span>
                        <span className={"text-hint"}>Создайте новый секрет для OAuth2 клиента</span>
                    </div>
                </div>
                <div className={styles.warningBlock}>
                    <TriangleAlert className={styles.warningIcon} size={30}/>
                    <div className={styles.headerTextWrapper}>
                        <span>После генерации старый secret станет недействительным.</span>
                        <span className={"text-hint"}>Сохраните новый client secret в безопасном месте.</span>
                    </div>
                </div>
                {clientSecret &&
                    <div className={`${styles.clientSecretWrapper} ${styles.copyableTextWrapper}`}>
                        <span>{clientSecret}</span>
                        <Copy className={styles.copyableTextIcon} size={18} onClick={() => copyText(clientSecret)}/>
                    </div>
                }
                <div className={styles.modalWindowButtons}>
                    <Button variant={"secondary"} onClick={() => modalRef.current?.close()}>Отмена</Button>
                    <Button onClick={generateSecret}>
                        Сгенерировать
                    </Button>
                </div>
            </dialog>
            <div className={styles.header}>
                <div className={styles.headerTextWrapper}>
                    <span
                        className={"headerTextPrimary"}>OAuth2 клиенты</span> {/*TODO: global property text-primary*/}
                    <span
                        className={"headerTextSecondary"}> Управляйте OAuth2 клиентами и их настройками </span> {/*TODO: global property text-secondary*/}
                </div>
                <Button className={styles.addButton} onClick={() => navigate("client")}>
                    Добавить клиента
                </Button>
            </div>
            <div className={styles.form}>
                <div className={styles.searchWrapper}>
                    <TextInput placeholder="Поиск клиентов..."
                               value={search}
                               className={styles.search}
                               onChange={(e) => setSearch(e.target.value)}>
                    </TextInput>
                    <Search className={styles.searchIcon} size={18}/>
                </div>
                <div className={styles.blockHeader}>
                    <span>Название клиента</span>
                    <span>Client ID</span>
                    <span>Дата создания</span>
                    <span>Действия</span>
                </div>
                {
                    pageResponse.content.length === 0 ? (
                        <div className={styles.emptyState}>
                            <span>Клиенты не найдены</span>
                        </div>
                    ) : (
                        pageResponse.content.map(client => (
                            <div key={client.clientId} className={styles.clientCard}>
                                <div className={styles.clientField}>
                                    <span>{client.clientName}</span>
                                </div>
                                <div className={`${styles.clientField} ${styles.copyableTextWrapper}`}>
                                    <span>{client.clientId}</span>
                                    <Copy className={styles.copyableTextIcon} size={18}
                                          onClick={() => copyText(client.clientId)}/>
                                </div>
                                <div className={styles.clientField}>
                                    <span>{client.createdAt && dateFormatter.format(client.createdAt)}</span>
                                </div>
                                <div className={styles.clientActions}>
                                    <button className={styles.actionButton}
                                            onClick={() => showModal(client.clientId)}>
                                        <BookKey size={18}/>
                                    </button>
                                    <button className={styles.actionButton}
                                            onClick={() => navigate(`client?clientId=${client.clientId}`)}>
                                        <Pencil size={18}/>
                                    </button>
                                    <button className={`${styles.actionButton} danger`}
                                            onClick={() => deleteClient(client.clientId)}>
                                        <Trash2 size={18}/>
                                    </button>
                                </div>
                            </div>
                        ))
                    )}
                <div className={styles.pagesBlock}>
                    <span
                        className={"text-hint"}>Показано {pageResponse.pageable.offset + 1}-{pageResponse.pageable.offset + pageResponse.numberOfElements} из {pageResponse.totalElements} клиентов</span>
                    <div className={styles.pageSwitcher}>
                        <ChevronLeft onClick={prevPage} className={styles.arrowIcon} size={25}/>
                        <span className={styles.pageNumber}>{page + 1}</span>
                        <ChevronRight onClick={nextPage} className={styles.arrowIcon} size={25}/>
                    </div>
                </div>
            </div>
        </div>
    );
}
