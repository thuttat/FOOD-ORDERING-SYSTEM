import { BrowserRouter, Routes, Route } from "react-router-dom";
import {Login} from "./pages/auth/Login.jsx";
import {Register} from "./pages/auth/Register.jsx";
import BaseLayout from "./layouts/BaseLayout.jsx";
import {Home} from "./pages/customer/Home.jsx";
import {AdminDashboard} from "./pages/admin/AdminDashboard.jsx";

function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route element={<BaseLayout />}>
            <Route path="/" element={<AdminDashboard />} />
          </Route>

        </Routes>
      </BrowserRouter>
  );
}

export default App;