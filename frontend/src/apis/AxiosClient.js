import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api";

const axiosClient = axios.create({
    baseURL: API_BASE_URL,
    timeout: 10000,
});

axiosClient.interceptors.request.use((config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
}, (err) => {
    return Promise.reject(err);
});

axiosClient.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 401) {
            console.error("Login session is expired! Please sign in again.");
            localStorage.removeItem("accessToken");
            localStorage.removeItem("role");
            window.location.href = "/";
        }

        return Promise.reject(error);
    }
);

export default axiosClient;