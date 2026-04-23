import axiosClient from "./AxiosClient.js";

const UserService = {
    getAllUsers: (params) => {
        return axiosClient.get("users", { params });
    },

    getStats: () => {
        return axiosClient.get("users/stats");
    },

    updateUser: (id, data) => {
        return axiosClient.patch(`users/${id}`, data);
    },

    updateStatus: (id, status) => {
        return axiosClient.put(`users/admin/${id}/status?status=${status}`);
    },

    updateRole: (id, role) => {
        return axiosClient.put(`users/admin/${id}/role?role=${role}`);
    }
};

export default UserService;