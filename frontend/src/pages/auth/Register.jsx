import {Button} from "../../components/common/Button.jsx";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";

export function Register() {
    const [formData, setFormData] = useState({
        fullname: "",
        username: "",
        email: "",
        password: "",
        confirm: "",
        role: "USER",
    });

    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (formData.password !== formData.confirm) {
            alert("Mật khẩu xác nhận không khớp! Vui lòng kiểm tra lại.");
            return;
        }

        try {
            const payloadToSend = {
                username: formData.username,
                email: formData.email,
                password: formData.password,
                fullname: formData.fullname,
                role: formData.role,
            };

            const response = await axios.post("http://localhost:8080/api/auth/register", payloadToSend);

            if (response.status === 201 || response.status === 200) {
                alert("Registration successful! Please log in.");
                navigate("/login");
            }
        } catch (error) {
            console.error("Lỗi từ backend:", error.response?.data);
            alert(error.response?.data?.message || "Registration failed. Please try again.");
        }
    };

    return (
        <div className="login-page">
            <div className="login-container">
                <div className="card login-card">
                    <div className="login-header">
                        <div className="login-icon">
                            ✍️
                        </div>

                        <h3>Create Account</h3>
                        <p className="text-muted">
                            Join us to start your order
                        </p>
                    </div>

                    <form className="login-form" onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label>Full name</label>
                            <input
                                type="text"
                                className="input"
                                placeholder="Enter your full name"
                                name="fullname"
                                value={formData.fullname}
                                onChange={handleChange}
                            />
                        </div>

                        <div className="form-group">
                            <label>Username</label>
                            <input
                                type="text"
                                className="input"
                                placeholder="Enter your username"
                                name="username"
                                value={formData.username}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Email</label>
                            <input
                                type="email"
                                className="input"
                                placeholder="you@example.com"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Password</label>
                            <input
                                type="password"
                                className="input"
                                placeholder="Enter your password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>Confirm Password</label>
                            <input
                                type="password"
                                className="input"
                                placeholder="Confirm your password"
                                name="confirm"
                                value={formData.confirm}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>I want to register as</label>
                            <select
                                className="input"
                                name="role"
                                value={formData.role}
                                onChange={handleChange}
                            >
                                <option value="USER">Customer (Order Food)</option>
                                <option value="RESTAURANT">Restaurant Owner (Sell Food)</option>
                            </select>
                        </div>
                        <Button type="submit" className="btn-primary">Sign Up</Button>
                    </form>

                    <div className="login-footer">
                        <p className="text-muted">
                            Already have an account?{" "}
                            <a href="/login" className="link-primary">
                                Sign in
                            </a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
}