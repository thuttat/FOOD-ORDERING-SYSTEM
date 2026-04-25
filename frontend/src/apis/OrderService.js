import axiosClient from "./AxiosClient.js";

export const OrderService = {
    getAdminOrders: (search = '', status = '', restaurant = '', page = 0, size = 100) => {
        let url = `/orders/admin?page=${page}&size=${size}`;
        if (search) url += `&search=${search}`;
        if (status && status !== 'all') url += `&status=${status.toUpperCase()}`;
        if (restaurant && restaurant !== 'all') url += `&restaurantName=${restaurant}`;
        return axiosClient.get(url);
    },

    getAdminStats: () => {
        return axiosClient.get('/orders/admin/stats');
    },

    updateOrderStatus: (orderId, status) => {
        return axiosClient.patch(`/orders/${orderId}/status?status=${status}`);
    }
}