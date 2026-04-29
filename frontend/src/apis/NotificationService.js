import api from "./AxiosClient";

export const NotificationService = {
    getMyNotifications: () => api.get("/notifications/me"),
    markAsRead: (id) => api.put(`/notifications/${id}/read`)
};