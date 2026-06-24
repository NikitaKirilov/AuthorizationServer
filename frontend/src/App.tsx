import LoginPage from "./pages/LoginPage/LoginPage.tsx";
import {createBrowserRouter, Outlet, RouterProvider} from "react-router-dom";
import RegistrationPage from "./pages/RegistrationPage/RegistrationPage.tsx";
import EmailVerificationPage from "./pages/EmailVerificationPage/EmailVerificationPage.tsx";
import NotFoundPage from "./pages/NotFoundPage/NotFoundPage.tsx";
import ConsentPage from "./pages/ConsentPage/ConsentPage.tsx";
import {UserProfilePage} from "./pages/UserProfilePage/UserProfilePage.tsx";
import OAuth2ClientsPage from "./pages/OAuth2ClientsPage/OAuth2ClientsPage.tsx";
import OAuth2ClientPage from "./pages/OAuth2ClientPage/OAuth2ClientPage.tsx";
import Sidebar from "./components/Sidebar/Sidebar.tsx";
import UserDevicePage from "./pages/UserDevicePage/UserDevicePage.tsx";
import AuthorizationPage from "./pages/AuthorizationPage/AuthorizationPage.tsx";
import UserAdminPage from "./pages/UserAdminPage/UserAdminPage.tsx";


const SidebarLayout = () => (
    <div className={"sidebar-layout"}>
        <Sidebar/>
        <Outlet/>
    </div>
);


export const router = createBrowserRouter([
    {
        path: "/app",
        children: [
            {
                element: <SidebarLayout/>,
                children: [
                    {
                        path: "user",
                        children: [
                            {
                                path: "profile",
                                element: <UserProfilePage/>,
                            },
                            {
                                path: "devices",
                                element: <UserDevicePage/>,
                            },
                            {
                                path: "authorizations",
                                element: <AuthorizationPage/>,
                            },
                        ],
                    },
                    {
                        path: "oauth2/clients",
                        children: [
                            {
                                path: "",
                                element: <OAuth2ClientsPage/>,
                            },
                            {
                                path: "client",
                                element: <OAuth2ClientPage/>,
                            },
                        ],
                    },
                    {
                        path: "admin",
                        children: [
                            {
                                path: "users",
                                element: <UserAdminPage/>,
                            },
                        ],
                    },
                ],
            },
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
        ],
    },
    {
        path: "*",
        element: <NotFoundPage/>,
    },
]);


function App() {
    return (
        <RouterProvider router={router}/>
    );
}

export default App;
