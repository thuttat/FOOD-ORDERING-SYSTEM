import api from "./AxiosClient";

export const OrderService = {
    placeOrder: (data) => api.post("/orders", data),

    getMyOrders: () => api.get("/orders/me"),

    getOrderDetail: (id) => api.get(`/orders/${id}`),

    getAdminOrders: (search = '', status = '', restaurant = '', page = 0, size = 100) => {
        let url = `/orders/admin?page=${page}&size=${size}`;
        if (search) url += `&search=${search}`;
        if (status && status !== 'all') url += `&status=${status.toUpperCase()}`;
        if (restaurant && restaurant !== 'all') url += `&restaurantName=${restaurant}`;
        return api.get(url);
    },

    getAdminStats: () => {
        return api.get('/orders/admin/stats');
    },

    updateOrderStatus: (id, status) => {
        return api.patch(`/orders/${id}/status?status=${status}`);
    }
};