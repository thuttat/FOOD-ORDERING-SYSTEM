import React from 'react';
import { XCircle, DollarSign } from 'lucide-react';
import { Button } from '../../../../components/common/Button';

export function OrderActionModal({ actionModal, setActionModal, handleActionConfirm }) {
    if (!actionModal) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-box">
                <div className="modal-header">
                    <div className={`modal-icon-wrapper ${actionModal.type === 'cancel' ? 'bg-destructive-light' : 'bg-warning-light'}`}>
                        {actionModal.type === 'cancel' ? <XCircle className="action-icon text-destructive" /> : <DollarSign className="action-icon text-warning" />}
                    </div>
                    <div>
                        <h3 className="modal-title">
                            {actionModal.type === 'cancel' ? 'Cancel Order' : actionModal.type === 'refund' ? 'Process Refund' : 'Contact'}
                        </h3>
                        <p className="modal-subtitle">Order #{actionModal.order.id}</p>
                    </div>
                </div>

                {actionModal.type !== 'contact' && (
                    <textarea className="modal-textarea" rows="3" placeholder={`Enter ${actionModal.type} reason...`} />
                )}

                <div className="modal-actions">
                    <Button className="btn-outline flex-1" onClick={() => setActionModal(null)}>
                        Cancel
                    </Button>
                    {actionModal.type !== 'contact' && (
                        <Button className={`flex-1 ${actionModal.type === 'cancel' ? 'btn-danger' : 'btn-primary'}`} onClick={handleActionConfirm}>
                            Confirm
                        </Button>
                    )}
                </div>
            </div>
        </div>
    );
}