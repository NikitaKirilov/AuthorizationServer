import React, {useState} from "react";
import "./ConsentPage.css";

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
        <div className="auth-form">
            <h1>Consent required</h1>

            <div className="description">
                <p className="description-text text-hint">{clientId} wants to access your account</p>
                <p className={"description-text text-hint"}>The following permissions are requested by the above app</p>
                <p className={"description-text text-hint"}>Please review these and consent if you approve</p>
            </div>

            <form method="post" action="/oauth2/authorize">
                <input type="hidden" name="client_id" value={clientId}/>
                <input type="hidden" name="state" value={state}/>

                {agreedScopes.map(scope =>
                    <input key={scope} type={"hidden"} name={"scope"} value={scope}/>,
                )}

                {scopes!.map(scope => (
                    <div className="scope" key={scope}>
                        <input
                            className="scope-checkbox"
                            type="checkbox"
                            value={scope}
                            onChange={handleChange}
                        />
                        <span>{scope}</span>
                    </div>
                ))}

                <button
                    className="default-button"
                    type="submit"
                    disabled={agreedScopes.length === 0}
                >
                    Submit
                </button>
            </form>

            <form method="post" action="/oauth2/authorize">
                <input type="hidden" name="client_id" value={clientId}/>
                <input type="hidden" name="state" value={state}/>

                <button
                    className="cancel-button text-hint"
                    type="submit"
                >
                    Cancel
                </button>
            </form>
        </div>
    );
}
