import React, { useEffect, useState } from "react";
import { CartService } from "../../../apis/CartService";
import { CartItem } from "./components/CartItem.jsx";
import { OrderSummary } from "./components/OrderSummary.jsx";
import "./Cart.css";

export function Cart() {
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);

    const fetchCartData = async () => {
        try {
            const response = await CartService.getCart();
            setCart(response.data);
        } catch (error) {
            console.error("Invalid data:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCartData();
    }, []);

    const updateQuantity = async (itemId, newQuantity) => {
        if (newQuantity < 1) return;
        try {
            await CartService.updateQuantity(itemId, newQuantity);
            fetchCartData();
        } catch (error) {
            console.error("Updated error:", error);
        }
    };

    const removeItem = async (itemId) => {
        if (window.confirm("Are you sure deleting this one?")) {
            try {
                await CartService.removeItem(itemId);
                fetchCartData();
            } catch (error) {
                console.error("Cannot delete:", error);
            }
        }
    };

    if (loading) return <div className="cart-loading">Loading...</div>;

    return (
        <div className="cart-page-container">
            <h2 className="cart-title">Your cart</h2>
            
            {cart?.items?.length > 0 ? (
                <div className="cart-main-layout">

                    <div className="cart-items-section">
                        {cart.items.map((item) => (
                            <CartItem
                                key={item.id}
                                item={item}
                                onUpdate={updateQuantity}
                                onRemove={removeItem}
                            />
                        ))}
                    </div>

                    <div className="cart-summary-section">
                        <OrderSummary total={cart.totalPrice} />
                    </div>
                </div>
            ) : (
                <div className="cart-empty-state">
                    <div className="empty-icon">🛒</div>
                    <p>Giỏ hàng của bạn hiện đang trống.</p>
                    <button 
                        className="btn-continue-shopping" 
                        onClick={() => window.location.href = "/"}
                    >
                        Continue
                    </button>
                </div>
            )}
        </div>
    );
}