import React, { useState } from "react";
import { X } from "lucide-react";
import { Input } from "../../../../components/common/Input";
import { Button } from "../../../../components/common/Button";
import {Select} from "../../../../components/common/Select.jsx";

export default function CreateUserModal({ isOpen, onClose, handleCreate }) {
    const [formData, setFormData] = useState({ fullname: '', username: '', email: '', phone: '' });

    if (!isOpen) return null;

    const onSubmit = () => {
        if (!formData.fullname || !formData.username || !formData.email) {
            alert("Please fill in all the information!"); return;
        }
        handleCreate({
            ...formData,
            password: "123456",
            role: (formData.role || 'RESTAURANT').toUpperCase()
        });
    };

    return (
        <div className="modal">
            <div className="modal-box">
                <div className="modal-header">
                    <h2>Create User</h2>
                    <button onClick={onClose}><X /></button>
                </div>
                <div style={{ marginBottom: '10px', fontSize: '13px', color: 'var(--muted-foreground)' }}>
                    * The default password will be: <strong>123456</strong><br/>
                </div>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                    <Input placeholder="Full name" value={formData.fullname} onChange={(e) => setFormData({...formData, fullname: e.target.value})} />
                    <Input placeholder="Username" value={formData.username} onChange={(e) => setFormData({...formData, username: e.target.value})} />
                    <Input placeholder="Email" type="email" value={formData.email} onChange={(e) => setFormData({...formData, email: e.target.value})} />
                    <Input placeholder="Phone number" value={formData.phone} onChange={(e) => setFormData({...formData, phone: e.target.value})} />
                    <div>
                        <div style={{ fontSize: '14px', marginBottom: '4px', color: 'var(--foreground)', fontWeight: 500 }}>
                            Role
                        </div>
                        <Select
                            options={[
                                { value: 'user', label: 'User' },
                                { value: 'restaurant', label: 'Restaurant' },
                                { value: 'admin', label: 'Admin' },
                            ]}
                            value={formData.role}
                            onChange={(e) => setFormData({...formData, role: e.target.value})}
                        />
                    </div>
                </div>

                <div className="modal-actions" style={{ marginTop: '16px' }}>
                    <Button className="btn-outline flex-1" onClick={onClose}>Cancel</Button>
                    <Button className="btn-primary flex-1" onClick={onSubmit}>Submit</Button>
                </div>
            </div>
        </div>
    );
}