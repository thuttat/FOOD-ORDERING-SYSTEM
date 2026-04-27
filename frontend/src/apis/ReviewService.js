import axiosClient from "./AxiosClient.js";

export const ReviewService = {
    getAllReviews: (params) => {
        return axiosClient.get('/admin/reviews', { params });
    },
    getStats: () => {
        return axiosClient.get('/admin/reviews/stats');
    },
    deleteReview: (id) => {
        return axiosClient.delete(`/admin/reviews/${id}`);
    }
};