import React from "react";
import { Card, CardContent } from "../../../../components/common/Card.jsx";
import { Button } from "../../../../components/common/Button.jsx";

export function MenuItemCard({ item, onAdd }) {
    const isAvailable = item.available !== false && item.isAvailable !== false;

    return (
        <Card hover={true} className="menu-item-card" style={{ height: '100%' }}>
            <CardContent>
                <div style={{ display: 'flex', gap: '16px' }}>
                    <div style={{ width: '120px', height: '120px', flexShrink: 0 }}>
                        <img
                            src={item.imageUrl || "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500&q=80"}
                            alt={item.name}
                            style={{ width: '100%', height: '100%', objectFit: 'cover', borderRadius: '12px' }}
                        />
                    </div>
                    <div style={{ flex: 1, display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                        <div>
                            <h4 style={{ margin: '0 0 8px 0', fontSize: '1.1rem', color: '#1e293b' }}>
                                {item.name}
                            </h4>
                            <p className="text-muted" style={{ margin: 0, fontSize: '0.9rem', display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                                {item.description || "Chưa có mô tả cho món ăn này."}
                            </p>
                        </div>

                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '12px' }}>
                            <span style={{ fontWeight: 'bold', fontSize: '1.1rem', color: '#e63946' }}>
                                {item.price?.toLocaleString()}đ
                            </span>
                            <Button
                                className={isAvailable ? "btn-primary" : ""}
                                onClick={() => onAdd(item.id)}
                                disabled={!isAvailable}
                                style={{ padding: '6px 16px', fontSize: '0.9rem' }}
                            >
                                {isAvailable ? "Add" : "Restore"}
                            </Button>
                        </div>
                    </div>
                </div>
            </CardContent>
        </Card>
    );
}