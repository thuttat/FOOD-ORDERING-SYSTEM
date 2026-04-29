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
            window.dispatchEvent(new Event('cartUpdated'));
        } catch (error) {
            console.error("cannot find data:", error);
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
            console.error("uadated error:", error);
        }
    };

    const removeItem = async (itemId) => {
        if (window.confirm("are you sure for deleting?")) {
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
                        <OrderSummary
                            total={cart.totalAmount}
                            cartData={cart}
                        />
                    </div>
                </div>
            ) : (
                <div className="cart-empty-state">
                    <p>Blank.</p>
                </div>
            )}
        </div>
    );
}