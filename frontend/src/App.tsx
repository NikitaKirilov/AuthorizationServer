import {Route, Routes} from "react-router";
import LoginPage from "./pages/LoginPage/LoginPage.tsx";
import {BrowserRouter} from "react-router-dom";
import RegistrationPage from "./pages/RegistrationPage/RegistrationPage.tsx";
import EmailVerificationPage from "./pages/EmailVerificationPage/EmailVerificationPage.tsx";
import NotFoundPage from "./pages/NotFoundPage/NotFoundPage.tsx";
import {useEffect} from "react";
import authApi from "./api/authApi.ts";

function App() {
    useEffect(() => {
        authApi.csrf().catch(console.error);
    }, []);

    return (
        <BrowserRouter>
            <Routes>
                <Route path={"app"}>
                    <Route path={"*"} element={<NotFoundPage/>}/>
                    <Route path={"login"} element={<LoginPage/>}/>
                    <Route path={"registrations"}>
                        <Route path={"new"} element={<RegistrationPage/>}/>
                        <Route path={"verify"} element={<EmailVerificationPage/>}/>
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
