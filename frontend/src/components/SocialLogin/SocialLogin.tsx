import styles from "./SocialLogin.module.css";

const socialLoginUrl = import.meta.env.VITE_SOCIAL_LOGIN_URL;

type SocialLoginProps = {
    providerName: string,
    registrationId: string,
    imageUrl?: string,
}

export default function SocialLogin({providerName, registrationId, imageUrl}: Readonly<SocialLoginProps>) {
    const redirect = `${socialLoginUrl + registrationId}`;
    return (
        <button className={styles.button} key={registrationId}
                onClick={() => globalThis.location.href = redirect}>
            {imageUrl && <img className={styles.image} alt={"img"} src={imageUrl}/>}
            Продолжить с {providerName}
        </button>
    );
}
