import React from "react";
import { X } from "lucide-react";
import { Input } from "../../../../components/common/Input";
import { Button } from "../../../../components/common/Button";

export default function EditUserModal({
                                          editingUser,
                                          setEditingUser,
                                          handleSaveUser
                                      }) {
    if (!editingUser) return null;

    return (
        <div className="modal">
            <div className="modal-box">

                <div className="modal-header">
                    <h2>Edit User</h2>
                    <button onClick={() => setEditingUser(null)}>
                        <X />
                    </button>
                </div>

                <Input
                    value={editingUser.fullname}
                    onChange={(e) =>
                        setEditingUser({ ...editingUser, fullname: e.target.value })
                    }
                />

                <Input
                    value={editingUser.email}
                    onChange={(e) =>
                        setEditingUser({ ...editingUser, email: e.target.value })
                    }
                />

                <Input
                    value={editingUser.phone}
                    onChange={(e) =>
                        setEditingUser({ ...editingUser, phone: e.target.value })
                    }
                />

                <div className="modal-actions">
                    <Button onClick={() => setEditingUser(null)}>Cancel</Button>
                    <Button onClick={handleSaveUser}>Save</Button>
                </div>

            </div>
        </div>
    );
}