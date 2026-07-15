import LoginPage from "./pages/LoginPage/LoginPage.tsx";
import {createBrowserRouter, Outlet, RouterProvider} from "react-router-dom";
import RegistrationPage from "./pages/RegistrationPage/RegistrationPage.tsx";
import EmailVerificationPage from "./pages/EmailVerificationPage/EmailVerificationPage.tsx";
import NotFoundPage from "./pages/NotFoundPage/NotFoundPage.tsx";
import ConsentPage from "./pages/ConsentPage/ConsentPage.tsx";
import {UserProfilePage} from "./pages/UserProfilePage/UserProfilePage.tsx";
import OAuth2ClientsPage from "./pages/OAuth2ClientsPage/OAuth2ClientsPage.tsx";
import OAuth2ClientEditPage from "./pages/OAuth2ClientEditPage/OAuth2ClientEditPage.tsx";
import Sidebar from "./components/Sidebar/Sidebar.tsx";
import UserDevicePage from "./pages/UserDevicePage/UserDevicePage.tsx";
import AuthorizationPage from "./pages/AuthorizationPage/AuthorizationPage.tsx";
import UserAdminPage from "./pages/UserAdminPage/UserAdminPage.tsx";
import UserAdminEditPage from "./pages/UserAdminEditPage/UserAdminEditPage.tsx";
import RoleAdminPage from "./pages/RoleAdminPage/RoleAdminPage.tsx";
import RoleAdminEditPage from "./pages/RoleAdminEditPage/RoleAdminEditPage.tsx";
import AuthorityAdminPage from "./pages/AuthorityAdminPage/AuthorityAdminPage.tsx";


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
                                element: <OAuth2ClientEditPage/>,
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
                            {
                                path: "users/:id",
                                element: <UserAdminEditPage/>,
                            },
                            {
                                path: "roles",
                                element: <RoleAdminPage/>,
                            },
                            {
                                path: "roles/role",
                                element: <RoleAdminEditPage/>,
                            },
                            {
                                path: "authorities",
                                element: <AuthorityAdminPage/>
                            }
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
