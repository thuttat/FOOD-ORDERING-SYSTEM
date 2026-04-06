import { Header } from "../components/common/Header.jsx";
import { Sidebar } from "../components/common/Sidebar.jsx";
import "./../styles/index.css";
import {Outlet} from "react-router-dom";
import {Footer} from "../components/common/Footer.jsx";

export function BaseLayout({ sidebarItems, role }) {
    return (
        <div>
            <Header role={role} />

            <div className="layout">
                <Sidebar items={sidebarItems} />

                <main className="content">
                    <div className="page-wrapper">
                        <Outlet />
                    </div>

                    <Footer />
                </main>
            </div>
        </div>
    );
}