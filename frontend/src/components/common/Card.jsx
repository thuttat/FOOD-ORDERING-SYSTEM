import React from "react";
import "../styles/Card.css";

export function Card({ children, className = "", onClick, hover = false }) {
    const hoverClass = hover ? "card-hover" : "";

    return (
        <div
            className={`card ${hoverClass} ${className}`}
            onClick={onClick}
        >
            {children}
        </div>
    );
}

export function CardHeader({ children, className = "" }) {
    return <div className={`card-header ${className}`}>{children}</div>;
}

export function CardContent({ children, className = "" }) {
    return <div className={`card-content ${className}`}>{children}</div>;
}