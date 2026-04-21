import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./services/AuthContext.jsx";
import PrivateRoute from "./pages/auth/PrivateRoute.jsx";

import { Login } from "./pages/auth/Login.jsx";
import { Register } from "./pages/auth/Register.jsx";

import { AdminLayout } from "./layouts/AdminLayout.jsx";
import { CustomerLayout } from "./layouts/CustomerLayout.jsx";
import { RestaurantLayout } from "./layouts/RestaurantLayout.jsx";

import { Home } from "./pages/customer/Home/Home.jsx";
import { RestaurantDetail } from "./pages/customer/RestaurantDetails/RestaurantDetail.jsx";
import { Cart } from "./pages/customer/Cart/Cart.jsx";
import { PaymentResult } from "./pages/customer/PaymentResult/PaymentResult.jsx";

import { AdminDashboard } from "./pages/admin/AdminDashboard/AdminDashboard.jsx";
import { RestaurantsManagement } from "./pages/admin/RestaurantsManagement/RestaurantsManagement.jsx";
import { ReviewsManagement } from "./pages/admin/ReviewsManagement/ReviewsManagement.jsx";

import DashboardRestaurants from "./pages/restaurant/DashboardRestaurants.jsx"; 
import MenuTable from "./pages/restaurant/components/MenuTable.jsx";
import RealtimeOrder from "./pages/restaurant/components/RealtimeOrder.jsx";

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />

                    <Route
                        element={
                            <PrivateRoute allowedRoles={["USER"]}>
                                <CustomerLayout />
                            </PrivateRoute>
                        }
                    >
                        <Route path="/" element={<Home />} />
                        <Route path="/restaurant/:id" element={<RestaurantDetail />} />
                        <Route path="/cart" element={<Cart />} />
                        <Route path="/payment-result" element={<PaymentResult />} />
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
                        <Route path="menu" element={<MenuTable />} />
                        <Route path="orders" element={<RealtimeOrder />} />
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
                        <Route path="restaurants" element={<RestaurantsManagement />} />
                        <Route path="reviews" element={<ReviewsManagement />} />
                    </Route>

                    <Route path="*" element={<Navigate to="/" replace />} />
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;