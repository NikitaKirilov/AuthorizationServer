import LoginButton from "../componentns/LoginButton.tsx";
import {jwtDecode} from "jwt-decode";

export default function HomePage() {
    const idToken = sessionStorage.getItem("id_token");

    if (idToken) {
        const decodedToken = jwtDecode(idToken);
        console.log(decodedToken);
    }

    return (
        <div className={"home-page"}>
            {idToken && <p>{idToken}</p>}
            <LoginButton/>
        </div>
    );
}
