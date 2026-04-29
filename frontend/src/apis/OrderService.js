import api from "./AxiosClient";

export const OrderService = {
    placeOrder: (data) => api.post("/orders", data),
    getMyOrders: () => api.get("/orders/me"),
    getOrderDetail: (id) => api.get(`/orders/${id}`),
    updateOrderStatus: (id, status) => api.patch(`/orders/${id}/status?status=${status}`)
};