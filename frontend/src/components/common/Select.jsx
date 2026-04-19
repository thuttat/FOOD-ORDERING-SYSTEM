import React from 'react';
import '../styles/Select.css';

export function Select({ label, options, className = '', ...props }) {
    return (
        <div className="select-wrapper">
            {label && (
                <label className="select-label">
                    {label}
                </label>
            )}

            <select
                className={`select ${className}`}
                {...props}
            >
                {options.map((option) => (
                    <option key={option.value} value={option.value}>
                        {option.label}
                    </option>
                ))}
            </select>
        </div>
    );
}