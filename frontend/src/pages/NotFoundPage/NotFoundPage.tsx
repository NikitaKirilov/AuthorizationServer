import "./NotFoundPage.css";
import {useEffect} from "react";

const TITLE = "Not Found";

export default function NotFoundPage() {
    useEffect(() => {
        document.title = TITLE;
    });

    return (
        <div className={"not-found-page"}>
            <h1>404</h1>
            <h1>PAGE NOT FOUND</h1>
        </div>
    );
}
