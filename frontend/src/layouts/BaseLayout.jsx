import { Outlet } from "react-router-dom";
import {Header} from "../components/common/Header.jsx";
import {Footer} from "../components/common/Footer.jsx";

export default function BaseLayout() {
    return (
        <div className="min-vh-100 d-flex flex-column" style={{ background: "var(--background)" }}>
            <Header />

            <main className="flex-grow-1">
                <Outlet />
            </main>

            <Footer />
        </div>
    );
}