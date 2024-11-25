import {useEffect, useState} from "react";
import {idpRegistrationApi} from "../../api/idpRegistrationApi.ts";
import {ClientRegistrationPublicInfo} from "../../types/ClientRegistrationPublicInfo.ts";
import "./SocialLogin.css";

export default function SocialLogin() {
    const [clientRegistrationPublicInfo, setClientRegistrationPublicInfo] = useState<Array<ClientRegistrationPublicInfo> | undefined>();

    useEffect(() => {
        idpRegistrationApi.getLoginLinks()
            .then((response) => {
                    setClientRegistrationPublicInfo(response);
                },
            );
    }, []);

    function getImage(image: string): string {
        return `data:image/png;base64,${image}`;
    }

    return (
        <>
            {
                clientRegistrationPublicInfo?.map(
                    (publicInfo: ClientRegistrationPublicInfo) => (
                        <button className={"idp-registration"} key={publicInfo.registrationId}
                                onClick={() => window.location.href = publicInfo.loginUri}>
                            {
                                publicInfo.image &&
                                <img className={"idp-registration-img"} alt={"img"} src={getImage(publicInfo.image)}/>
                            }
                            <p className={"idp-registration-name"}>Continue with {publicInfo.clientName}</p>
                        </button>
                    ),
                )
            }
        </>
    );
}
