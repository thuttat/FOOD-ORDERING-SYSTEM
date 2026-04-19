import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./services/AuthContext.jsx";
import PrivateRoute from "./pages/auth/PrivateRoute.jsx";

// Auth Pages
import { Login } from "./pages/auth/Login.jsx";
import { Register } from "./pages/auth/Register.jsx";

// Layouts
import { AdminLayout } from "./layouts/AdminLayout.jsx";
import { CustomerLayout } from "./layouts/CustomerLayout.jsx";
import { RestaurantLayout } from "./layouts/RestaurantLayout.jsx";

// Customer Pages
import { Home } from "./pages/customer/Home/Home.jsx";

// Admin Pages (Theo cấu trúc Folder-per-page)
import { AdminDashboard } from "./pages/admin/AdminDashboard/AdminDashboard.jsx";
import { RestaurantsManagement } from "./pages/admin/RestaurantsManagement/RestaurantsManagement.jsx";
import { ReviewsManagement } from "./pages/admin/ReviewsManagement/ReviewsManagement.jsx";

// Restaurant Pages (Tích hợp chức năng mới)
import DashboardRestaurants from "./pages/restaurant/DashboardRestaurants/DashboardRestaurants.jsx"; 
import RealtimeOrder from "./pages/restaurant/Orders/RealtimeOrder.jsx";
import MenuTable from "./pages/restaurant/Menu/MenuTable.jsx";

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />


                    <Route element={<CustomerLayout />}>
                        <Route path="/" element={<Home />} />
   
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