import React, {useState} from "react";
import styles from "./ConsentPage.module.css";
import {Button} from "../../components/Button/Button.tsx";
import Page from "../../components/Page/Page.tsx";
import Form from "../../components/Form/Form.tsx";

export default function ConsentPage() {
    const params = new URLSearchParams(globalThis.location.search);

    const state = params.get("state");
    const clientId = params.get("client_id");
    const scope = params.get("scope");
    const scopes = scope?.split(" ");
    const [agreedScopes, setAgreedScopes] = useState<string[]>([]);

    if (!state || !clientId || !scope) {
        globalThis.location.href = "/app/login";
        return null;
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {value, checked} = e.target;

        setAgreedScopes(prev =>
            checked
                ? [...prev, value]
                : prev.filter(s => s !== value),
        );
    };

    return (
        <Page className={styles.page}>
            <Form className={styles.form}>
                <h1>Требуется согласие</h1>

                <div className={styles.description}>
                    <p className="description-text text-hint">Приложение {clientId} запрашивает доступ к вашей учетной
                        записи</p>
                    <p className={"description-text text-hint"}>Для работы приложению необходимы следующие
                        разрешения</p>
                    <p className={"description-text text-hint"}>Ознакомьтесь с запрашиваемыми разрешениями и подтвердите
                        доступ, если вы доверяете этому приложению.</p>
                </div>

                <form method="post" action="/oauth2/authorize" className={styles.scopeForm}>
                    <input type="hidden" name="client_id" value={clientId}/>
                    <input type="hidden" name="state" value={state}/>

                    {agreedScopes.map(scope =>
                        <input key={scope} type={"hidden"} name={"scope"} value={scope}/>,
                    )}

                    {scopes!.map(scope => (
                        <div className={styles.scope} key={scope}>
                            <input
                                className={styles.scopeCheckbox}
                                type="checkbox"
                                value={scope}
                                onChange={handleChange}
                            />
                            <span>{scope}</span>
                        </div>
                    ))}
                    <Button className={styles.submitButton} variant={"primary"} type={"submit"}
                            disabled={agreedScopes.length === 0}>
                        Подтвердить
                    </Button>
                </form>

                <form method="post" action="/oauth2/authorize">
                    <input type="hidden" name="client_id" value={clientId}/>
                    <input type="hidden" name="state" value={state}/>

                    <button
                        className={`${styles.cancelButton} text-hint`}
                        type="submit"
                    >
                        Отменить
                    </button>
                </form>
            </Form>
        </Page>
    );
}
