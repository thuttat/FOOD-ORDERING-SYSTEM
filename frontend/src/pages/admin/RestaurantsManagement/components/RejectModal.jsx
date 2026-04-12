import React from 'react';
import { AlertCircle } from 'lucide-react';
import { Button } from '../../../../components/common/Button';

export function RejectModal({ onClose, onConfirm }) {
    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <div className="modal-header">
                    <div className="modal-icon-wrapper">
                        <AlertCircle className="modal-icon text-destructive" />
                    </div>
                    <div>
                        <h3 className="modal-title">Reject Restaurant</h3>
                        <p className="modal-subtitle">Are you sure you want to reject this restaurant?</p>
                    </div>
                </div>
                <div className="modal-actions">
                    <Button className="flex-1" onClick={onClose}>
                        Cancel
                    </Button>
                    <Button className="btn-danger flex-1" onClick={onConfirm}>
                        Confirm Rejection
                    </Button>
                </div>
            </div>
        </div>
    );
}