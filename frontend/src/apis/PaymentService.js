import api from "./AxiosClient";

export const PaymentService = {
    createPayment: (paymentData) => {
        return api.post("/payments/create", paymentData);
    },


    verifyPayment: (params) => {
        return api.get("/payments/callback", { params });
    },


    getPaymentHistory: () => {
        return api.get("/payments/history");
    }
};