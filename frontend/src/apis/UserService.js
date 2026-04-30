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

    createUserByAdmin: (data) => {
        return axiosClient.post("/users/admin/create", data);
    },

    updateStatus: (id, status) => {
        return axiosClient.put(`users/admin/${id}/status?status=${status}`);
    },

    updateRole: (id, role) => {
        return axiosClient.put(`users/admin/${id}/role?role=${role}`);
    },
    getUserProfile: (id) => axiosClient.get(`/users/${id}`),
    
    updateProfile: (id, data) => axiosClient.patch(`/users/${id}`, data)
};

export default UserService;