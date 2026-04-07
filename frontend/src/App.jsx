import {BrowserRouter, Routes, Route, Navigate} from "react-router-dom";
import {Login} from "./pages/auth/Login.jsx";
import {Register} from "./pages/auth/Register.jsx";
import {AdminDashboard} from "./pages/admin/AdminDashboard.jsx";
import {AdminLayout} from "./layouts/AdminLayout.jsx";
import {AdminRestaurants} from "./pages/admin/AdminRestaurants.jsx";
import PrivateRoute from "./pages/auth/PrivateRoute.jsx";
import {CustomerLayout} from "./layouts/CustomerLayout.jsx";
import {Home} from "./pages/customer/Home.jsx";
import {RestaurantLayout} from "./layouts/RestaurantLayout.jsx";

function App() {
  return (
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
                <Route index element={<Home />} />
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
                {/*<Route path="dashboard" element={<RestaurantDashboard />} />*/}
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