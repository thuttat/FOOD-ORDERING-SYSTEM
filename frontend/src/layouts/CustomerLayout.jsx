import { Header } from "../components/common/Header.jsx";
import {Outlet} from "react-router-dom";
import {Footer} from "../components/common/Footer.jsx";

export function CustomerLayout() {
    return (
        <div>
            <Header
                role="CUSTOMER"
                showSearch
                showNav
                showCart
            />

            <main className="container">
                <Outlet />
            </main>

            <Footer />
        </div>
    );
}