import React, { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { PaymentService } from "../../../apis/PaymentService";

export function PaymentResult() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const [status, setStatus] = useState("processing");

    useEffect(() => {
        const verify = async () => {
            const vnpCode = searchParams.get("vnp_ResponseCode");
            const momoCode = searchParams.get("resultCode");

            if (vnpCode === "00" || momoCode === "0") {
                setStatus("success");
                window.dispatchEvent(new Event('cartUpdated'));
                window.dispatchEvent(new Event('notifyUpdated'));
            } else {
                setStatus("fail");
            }
        };
        verify();
    }, [searchParams]);

    return (
        <div className="payment-result-page" style={{ textAlign: 'center', padding: '100px 20px' }}>
            {status === "success" ? (
                <div className="success-ui">
                    <h1 style={{ color: '#28a745', fontSize: '64px' }}>❤️‍</h1>
                    <h2>Successfully!</h2>
                    <p>Thanks darling we are preparing</p>
                </div>
            ) : (
                <div className="fail-ui">
                    <h1 style={{ color: '#dc3545', fontSize: '64px' }}>❌</h1>
                    <h2>Fail to pay</h2>
                    <p>Pls check your card.</p>
                </div>
            )}
            <button
                onClick={() => navigate("/orders")}
                style={{ marginTop: '20px', padding: '10px 25px', backgroundColor: '#ff4757', color: '#fff', border: 'none', borderRadius: '5px', cursor: 'pointer' }}
            >
                Check your history
            </button>
        </div>
    );
}