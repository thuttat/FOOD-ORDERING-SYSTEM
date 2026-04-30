import api from "./AxiosClient";

export const ReviewService = {
    submitReview: (reviewData) => api.post("/reviews", reviewData),

    getRestaurantReviews: (restaurantId) => api.get(`/reviews/restaurant/${restaurantId}`),

    getAllReviews: (params) => {
        return api.get('/admin/reviews', { params });
    },

    getStats: () => {
        return api.get('/admin/reviews/stats');
    },

    deleteReview: (id) => {
        return api.delete(`/admin/reviews/${id}`);
    }
};