import "./App.css";
import {Route, Routes} from "react-router";
import LoginPage from "./pages/LoginPage.tsx";
import {BrowserRouter} from "react-router-dom";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage/>}/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
