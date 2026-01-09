import {Route, Routes} from "react-router";
import LoginPage from "./pages/LoginPage/LoginPage.tsx";
import {BrowserRouter} from "react-router-dom";
import RegistrationPage from "./pages/RegistrationPage/RegistrationPage.tsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/registrations/new" element={<RegistrationPage/>}/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
