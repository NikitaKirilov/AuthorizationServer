import LoginPage from "./pages/LoginPage/LoginPage.tsx";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import RegistrationPage from "./pages/RegistrationPage/RegistrationPage.tsx";
import EmailVerificationPage from "./pages/EmailVerificationPage/EmailVerificationPage.tsx";
import NotFoundPage from "./pages/NotFoundPage/NotFoundPage.tsx";
import ConsentPage from "./pages/ConsentPage/ConsentPage.tsx";
import {UserProfilePage} from "./pages/UserProfilePage/UserProfilePage.tsx";
import OAuth2ClientsPage from "./pages/OAuth2ClientsPage/OAuth2ClientsPage.tsx";
import OAuth2ClientPage from "./pages/OAuth2ClientPage/OAuth2ClientPage.tsx";


export const router = createBrowserRouter([
    {
        path: "/app",
        children: [
            {
                path: "login",
                element: <LoginPage/>,
            },
            {
                path: "consent",
                element: <ConsentPage/>,
            },
            {
                path: "registrations",
                children: [
                    {
                        path: "new",
                        element: <RegistrationPage/>,
                    },
                    {
                        path: "verify",
                        element: <EmailVerificationPage/>,
                    },
                ],
            },
            {
                path: "user",
                children: [
                    {
                        path: "profile",
                        element: (
                            <UserProfilePage/>
                        ),
                    },
                ],
            },

            {
                path: "oauth2/clients",
                element: (
                    <OAuth2ClientsPage/>
                ),
            },
            {
                path: "oauth2/clients/client",
                element: (
                    <OAuth2ClientPage/>
                ),
            },
            {
                path: "*",
                element: <NotFoundPage/>,
            },
        ],
    },
]);

function App() {
    return (
        <RouterProvider router={router}/>
    );
}

export default App;
