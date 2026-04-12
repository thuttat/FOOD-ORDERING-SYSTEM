import React from "react";
import "../styles/Badge.css";

export function Badge({ status, children }) {
    return (
        <span className={`badge badge-${status}`}>
      {children}
    </span>
    );
}