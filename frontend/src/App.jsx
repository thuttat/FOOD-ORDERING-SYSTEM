import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { Login } from "./pages/auth/Login.jsx";
import { Register } from "./pages/auth/Register.jsx";
import { AdminDashboard } from "./pages/admin/AdminDashboard.jsx";
import { AdminLayout } from "./layouts/AdminLayout.jsx";
import { AdminRestaurants } from "./pages/admin/AdminRestaurants.jsx";
import PrivateRoute from "./pages/auth/PrivateRoute.jsx";
import { CustomerLayout } from "./layouts/CustomerLayout.jsx";

// import { Home } from "./pages/customer/Home/Home.jsx";

import { RestaurantLayout } from "./layouts/RestaurantLayout";
import DashboardRestaurants from "./pages/restaurant/pages/DashboardRestaurants";
import MenuTable from "./pages/restaurant/components/menu/MenuTable.jsx";
import RealtimeOrder from "./pages/restaurant/pages/RealtimeOrder";
import OrderHistory from "./pages/restaurant/pages/OrderHistory";
import Analytics from "./pages/restaurant/pages/Analytics.jsx";


function App() {
    return (
        <BrowserRouter>
            <Routes>

                <Route path="/" element={<Navigate to="/login" replace />} />

                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route
                    element={
                        <PrivateRoute allowedRoles={["USER"]}>
                            <CustomerLayout />
                        </PrivateRoute>
                    }
                >
                    {/* <Route index element={<Home />} /> */}
                </Route>

                <Route
                    path="/restaurant/*"
                    element={
                        <PrivateRoute allowedRoles={["RESTAURANT"]}>
                            <RestaurantLayout />
                        </PrivateRoute>
                    }
                >
                    <Route index element={<Navigate to="dashboard" replace />} />
                    <Route path="dashboard" element={<DashboardRestaurants />} />
                    <Route path="orders" element={<RealtimeOrder />} />
                    <Route path="menu" element={<MenuTable />} />
                    <Route path="history" element={<OrderHistory />} />
                    <Route path="analytics" element={<Analytics />} />
                </Route>

                <Route
                    path="/admin/*"
                    element={
                        <PrivateRoute allowedRoles={["ADMIN"]}>
                            <AdminLayout />
                        </PrivateRoute>
                    }
                >
                    <Route index element={<Navigate to="dashboard" replace />} />
                    <Route path="dashboard" element={<AdminDashboard />} />
                    <Route path="restaurants" element={<AdminRestaurants />} />
                </Route>
            </Routes>
        </BrowserRouter>
    );

}
export default App;