import {BrowserRouter, Routes, Route, Navigate} from "react-router-dom";
import {Login} from "./pages/auth/Login.jsx";
import {Register} from "./pages/auth/Register.jsx";
import {AdminDashboard} from "./pages/admin/AdminDashboard/AdminDashboard.jsx";
import {AdminLayout} from "./layouts/AdminLayout.jsx";
import {RestaurantsManagement} from "./pages/admin/RestaurantsManagement/RestaurantsManagement.jsx";
import PrivateRoute from "./pages/auth/PrivateRoute.jsx";
import {CustomerLayout} from "./layouts/CustomerLayout.jsx";
import {Home} from "./pages/customer/Home.jsx";
import {RestaurantLayout} from "./layouts/RestaurantLayout.jsx";
import {ReviewsManagement} from "./pages/admin/ReviewsManagement/ReviewsManagement.jsx";
import {AuthProvider} from "./services/AuthContext.jsx";
import {UsersManagement} from "./pages/admin/UsersManagement/UsersManagement.jsx";
import {AdminOrders} from "./pages/admin/AdminOrders/AdminOrders.jsx";

function App() {
  return (
      <AuthProvider>
          <BrowserRouter>
              <Routes>
                  <Route path="/login" element={<Login />} />
                  <Route path="/register" element={<Register />} />
                  <Route element={<CustomerLayout />}>
                      <Route path="/" element={<Home />} />
                      <Route element={<PrivateRoute allowedRoles={["USER"]} />}>
                          {/*<Route index element={<Navigate to="/" replace />} />*/}
                          {/*<Route path="/" element={<Home />} />*/}
                      </Route>
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
                      <Route path="restaurants" element={<RestaurantsManagement />} />
                      <Route path="reviews" element={<ReviewsManagement />} />
                      <Route path="users" element={<UsersManagement />} />
                      <Route path="orders" element={<AdminOrders />} />
                  </Route>
              </Routes>
          </BrowserRouter>
      </AuthProvider>
  );
}

export default App;