import axiosClient from "./AxiosClient.js";

const AdminDashboardService = {
    getDashboardStats: () => {
        return axiosClient.get('/admin/dashboard/stats');
    }
};

export default AdminDashboardService;