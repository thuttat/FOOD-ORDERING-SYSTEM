import api from "./AxiosClient";

export const CartService = {
    getCart: () => api.get("/cart"),

    addToCart: (menuItemId, quantity) =>
        api.post("/cart/items", { menuItemId, quantity }),

    updateQuantity: (cartItemId, quantity) =>
        api.put(`/cart/items/${cartItemId}`, { quantity }),

    removeItem: (cartItemId) =>
        api.delete(`/cart/items/${cartItemId}`),

    clearCart: () => api.delete("/cart")
};