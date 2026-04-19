import React from 'react';
import { X, Image as ImageIcon } from 'lucide-react';
import { Button } from '../../../../components/common/Button';
import { Card, CardContent } from '../../../../components/common/Card';

export function DetailPanel({restaurant, activeTab, onClose, onApprove, onRejectClick, onSuspend, onReinstate}) {
    return (
        <div className="side-panel-overlay">
            <div className="side-panel">
                <div className="panel-header">
                    <div>
                        <h2 className="panel-title">Restaurant Details</h2>
                        <p className="panel-subtitle">
                            {activeTab === 'pending' ? 'Review submission for approval' : 'Detailed restaurant information'}
                        </p>
                    </div>
                    <button onClick={onClose} className="close-btn">
                        <X className="w-5 h-5" />
                    </button>
                </div>

                <Card>
                    <CardContent className="detail-card-content">
                        <div>
                            <h3 className="detail-title">Basic Information</h3>
                            <div className="info-grid">
                                <div className="info-row">
                                    <span className="info-label">Restaurant Name:</span>
                                    <span className="info-value">{restaurant.name}</span>
                                </div>
                                <div className="info-row">
                                    <span className="info-label">Owner:</span>
                                    <span className="info-value">{restaurant.ownerName || 'N/A'}</span>
                                </div>
                                <div className="info-row">
                                    <span className="info-label">Phone number:</span>
                                    <span className="info-value">{restaurant.phoneNumber || 'N/A'}</span>
                                </div>
                                <div className="info-row">
                                    <span className="info-label">Address:</span>
                                    <span className="info-value">{restaurant.address || 'N/A'}</span>
                                </div>
                                <div className="info-row">
                                    <span className="info-label">Description:</span>
                                    <span className="info-value">{restaurant.description || 'N/A'}</span>
                                </div>
                                {activeTab === 'suspended' && restaurant.reason && (
                                    <div className="info-row mt-2 p-2 bg-destructive/10 rounded-md">
                                        <span className="info-label text-destructive font-medium">Suspension Reason:</span>
                                        <span className="info-value text-destructive">{restaurant.reason}</span>
                                    </div>
                                )}
                            </div>
                        </div>
                    </CardContent>
                </Card>

                <Card>
                    <CardContent className="detail-card-content">
                        <div className="section-header">
                            <ImageIcon className="section-icon" />
                            <h3 className="section-title">Media</h3>
                        </div>
                        <div className="media-grid">
                            {restaurant?.imageUrl ? (
                                <img src={restaurant.imageUrl} alt="Restaurant" className="media-image" />
                            ) : (
                                <div className="col-span-3 text-sm text-muted text-center py-2">No media available</div>
                            )}
                        </div>
                    </CardContent>
                </Card>

                <div className="panel-actions">
                    {activeTab === 'pending' && (
                        <>
                            <Button className="btn-approve" style={{ width: '50%' }} onClick={() => onApprove(restaurant.id)}>
                                Approve Restaurant
                            </Button>
                            <Button
                                className="btn-danger"
                                style={{ width: '50%' }}
                                onClick={() => onRejectClick(restaurant.id)}
                            >
                                Reject
                            </Button>
                        </>
                    )}

                    {activeTab === 'active' && (
                        <Button
                            className="btn-danger"
                            style={{ width: '100%' }}
                            onClick={() => onSuspend(restaurant.id)}
                        >
                            Suspend Restaurant
                        </Button>
                    )}

                    {activeTab === 'suspended' && (
                        <Button
                            className="btn-approve"
                            style={{ width: '100%' }}
                            onClick={() => onReinstate(restaurant.id)}
                        >
                            Reinstate Restaurant
                        </Button>
                    )}
                </div>
            </div>
        </div>
    );
}