import React from 'react';
import { PaymentService } from '../../../../apis/PaymentService';

export function OrderSummary({ total }) {
    const shippingFee = 15000; 
    const grandTotal = total + shippingFee;

    const handleCheckout = async () => {
        try {
       
            const paymentData = {
                amount: grandTotal,
                paymentMethod: "VNPAY", 
                description: "Pay"
            };

            const response = await PaymentService.createPayment(paymentData);
            
            if (response.data && response.data.paymentUrl) {
                window.location.href = response.data.paymentUrl;
            } else {
                alert("cannot receipt url.");
            }
        } catch (error) {
            console.error("Lỗi thanh toán:", error);
            alert("cannot connect ttothe paid gate");
        }
    };

    return (
        <div className="order-summary-card">
            <h3>sumary detail</h3>
            <div className="summary-details">
                <div className="summary-line">
                    <span>total:</span>
                    <span>{total.toLocaleString()}đ</span>
                </div>
                <div className="summary-line">
                    <span>Shipping fee:</span>
                    <span>{shippingFee.toLocaleString()}đ</span>
                </div>
                <hr />
                <div className="summary-line total">
                    <span>Total:</span>
                    <span>{grandTotal.toLocaleString()}đ</span>
                </div>
            </div>
            
            <button className="btn-checkout-now" onClick={handleCheckout}>
                Start to pay
            </button>
            
            <p className="payment-note">
                * Pay through momo or vnpay
            </p>
        </div>
    );
}