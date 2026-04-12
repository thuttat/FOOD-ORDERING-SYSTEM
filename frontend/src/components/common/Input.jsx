import React from "react";
import "../styles/Input.css";

export function Input({ label, error, icon, className = "", ...props }) {
    return (
        <div className="input-wrapper">
            {label && <label className="input-label">{label}</label>}

            <div className="input-container">
                {icon && <div className="input-icon">{icon}</div>}

                <input
                    className={`input-field 
            ${icon ? "has-icon" : ""} 
            ${error ? "input-error" : ""} 
            ${className}`}
                    {...props}
                />
            </div>

            {error && <p className="input-error-text">{error}</p>}
        </div>
    );
}