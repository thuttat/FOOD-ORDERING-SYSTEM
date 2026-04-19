import React, { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { PaymentService } from "../../../apis/PaymentService";
import { Button } from "../../../components/common/Button.jsx";
import "./PaymentResult.css";

export function PaymentResult() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const [status, setStatus] = useState("processing");

    useEffect(() => {
        const vnpResponseCode = searchParams.get("vnp_ResponseCode");
        
        if (vnpResponseCode === "00") {
            setStatus("success");
        } else {
            setStatus("fail");
        }
    }, [searchParams]);

    return (
        <div className="result-container">
            {status === "success" ? (
                <div className="status-box success">
                    <div className="icon">✅</div>
                    <h2>Successfully!</h2>
                    <p>Your bill are reparing.</p>
                </div>
            ) : (
                <div className="status-box fail">
                    <div className="icon">❌</div>
                    <h2>Fail to pay</h2>
                    <p>pls try other method.</p>
                </div>
            )}
            <Button className="btn-primary" onClick={() => navigate("/orders")}>
                Xem đơn hàng của tôi
            </Button>
        </div>
    );
}