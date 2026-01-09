import "./SocialLogin.css";

const socialLoginUrl = import.meta.env.VITE_SOCIAL_LOGIN_URL;

type SocialLoginProps = {
    providerName: string,
    registrationId: string,
    imageUrl?: string,
}

export default function SocialLogin({providerName, registrationId, imageUrl}: Readonly<SocialLoginProps>) {
    return (
        <button className={"idp-registration"} key={registrationId}
                onClick={() => globalThis.location.href = socialLoginUrl + registrationId}>
            {imageUrl && <img className={"idp-registration-img"} alt={"img"} src={imageUrl}/>}
            <p className={"idp-registration-name"}>Continue with {providerName}</p>
        </button>
    );
}
