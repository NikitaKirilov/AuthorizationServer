import "./App.css"
import {BrowserRouter} from "react-router-dom";
import {Route, Routes} from "react-router";
import HomePage from "./pages/HomePage.tsx";
import Callback from "./componentns/Callback.tsx";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path={"/home"} element={<HomePage/>}/>
                <Route path={"/app/code"} element={<Callback/>}/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
