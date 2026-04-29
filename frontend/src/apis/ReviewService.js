import api from "./AxiosClient";

export const ReviewService = {
    submitReview: (reviewData) => api.post("/reviews", reviewData),

    getRestaurantReviews: (restaurantId) => api.get(`/reviews/restaurant/${restaurantId}`)
};