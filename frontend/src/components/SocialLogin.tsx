import {useEffect, useState} from "react";
import {idpRegistrationApi, OAuth2LoginInfo} from "../api/idpRegistrationApi.ts";

export default function SocialLogin() {
    const [oAth2LoginInfo, setOAuth2LoginInfo] = useState<Array<OAuth2LoginInfo> | undefined>();

    useEffect(() => {
        idpRegistrationApi.getLoginLinks()
            .then((response) => {
                    setOAuth2LoginInfo(response);
                },
            );
    }, []);

    return (
        <>
            <h1>Or you can use Social Login</h1>
            <div>
                {
                    oAth2LoginInfo !== undefined && oAth2LoginInfo.map(
                        (loginInfo: OAuth2LoginInfo) => (
                            <div>
                                <a href={loginInfo.loginUri}>Continue with {loginInfo.clientName}</a>
                            </div>
                        ),
                    )
                }
            </div>
        </>
    );
}
