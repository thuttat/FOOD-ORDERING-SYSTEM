import axiosClient from "./AxiosClient.js";

export const RestaurantService = {
    getRestaurants: (page = 0, size = 1000) => {
        return axiosClient.get(`/admin/restaurants?page=${page}&size=${size}`);
    },

    createRestaurant: (data) => {
        return axiosClient.post('/admin/restaurants', data);
    },

    lockRestaurant: (id) => {
        return axiosClient.patch(`/admin/restaurants/${id}/lock`, {})
    },

    reinstateRestaurant: (id) => {
        return axiosClient.patch(`/admin/restaurants/${id}/reinstate`, {})
    }
}