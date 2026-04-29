import api from "./AxiosClient";

export const PaymentService = {
    createPayment: (paymentData) => api.post("/payments", paymentData),

    verifyMomo: (params) => api.get("/payments/momo-callback", { params }),

    verifyVnpay: (params) => api.get("/payments/vnpay-callback", { params }),
};