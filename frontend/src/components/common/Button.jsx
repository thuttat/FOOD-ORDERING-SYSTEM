import "./../styles/Button.css";

export function Button({ children, className = "", disabled = false, ...props }) {
    return (
        <button className={`btn ${className}`} disabled={disabled} {...props}>
            {children}
        </button>
    );
}