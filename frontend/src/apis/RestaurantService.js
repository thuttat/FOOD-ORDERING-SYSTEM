import axiosClient from "./AxiosClient.js";

export const RestaurantService = {
    getRestaurants: (page = 0, size = 1000) => {
        return axiosClient.get(`/admin/restaurants?page=${page}&size=${size}`);
    },

    approveRestaurant: (id) => {
        return axiosClient.patch(`/admin/restaurants/${id}/approve`, {})
    },

    lockRestaurant: (id) => {
        return axiosClient.patch(`/admin/restaurants/${id}/lock`, {})
    },

    reinstateRestaurant: (id) => {
        return axiosClient.patch(`/admin/restaurants/${id}/reinstate`, {})
    }
}