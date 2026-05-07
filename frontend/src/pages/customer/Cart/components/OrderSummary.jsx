import React, { useState } from 'react';
import { PaymentService } from '../../../../apis/PaymentService';
import { OrderService } from '../../../../apis/OrderService';

export function OrderSummary({ total = 0, cartData }) {
    const [address, setAddress] = useState("");
    const [method, setMethod] = useState('VNPAY');
    const [loading, setLoading] = useState(false);
    const shippingFee = 15000;
    const grandTotal = (Number(total) || 0) + shippingFee;

    const handleCheckout = async () => {
        if (!address.trim()) return alert("Please fill delivery address!");

        if (!cartData || !cartData.items || cartData.items.length === 0) {
            return alert("Your cart is empty!");
        }

        if (loading) return;

        try {
            setLoading(true);
            const resId = cartData.restaurantId || cartData.items?.[0]?.menuItem?.restaurantId || 1;

            const orderRes = await OrderService.placeOrder({ 
                restaurantId: resId, 
                deliveryAddress: address,
                paymentMethod: method 
            });

            const payRes = await PaymentService.createPayment({ 
                orderId: orderRes.data.id, 
                method: method,
                amount: grandTotal 
            });

            if (method === 'COD') {
                window.location.href = "/orders";
            } else  {
               const redirectUrl = payRes.data?.url || payRes.data?.paymentUrl;
                if (redirectUrl) {
                    window.location.href = redirectUrl;
                }
            }
        } catch (error) {
            console.error("Payment error:", error);
            alert("Checkout failed. Please try again!");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="order-summary-card">
            <h3>Summary detail</h3>
            <div className="summary-line"><span>Subtotal:</span><span>{total.toLocaleString()}đ</span></div>
            <div className="summary-line"><span>Shipping:</span><span>{shippingFee.toLocaleString()}đ</span></div>
            <hr />
            <div className="summary-line total"><span>Total:</span><span>{grandTotal.toLocaleString()}đ</span></div>

            <input
                type="text"
                placeholder="Delivery Address..."
                value={address}
                onChange={(event) => setAddress(event.target.value)}
                className="form-control my-3"
            />

            <div className="methods my-3">
                {['VNPAY', 'MOMO', 'COD'].map(m => (
                    <label key={m} className="d-block cursor-pointer">
                        <input type="radio" name="payMethod" checked={method === m} onChange={() => setMethod(m)} /> {m}
                    </label>
                ))}
            </div>

            <button
                className="btn-checkout-now w-100"
                onClick={handleCheckout}
                disabled={loading || total === 0}
            >
                {loading ? "Processing..." : "Order & Pay Now"}
            </button>
        </div>
    );
}