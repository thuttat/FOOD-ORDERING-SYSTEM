import axios from "axios";

const axiosClient = axios.create({
    baseURL: 'http://localhost:8081/api',
    headers: {
        "Content-Type": "application/json",
    }
});

axiosClient.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
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
            localStorage.removeItem("token");
            localStorage.removeItem("role");
            window.location.href = "/";
        }

        return Promise.reject(error);
    }
);

export default axiosClient;