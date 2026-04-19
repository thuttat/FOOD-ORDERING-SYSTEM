import React from 'react';
import { Badge } from '../../../../components/common/Badge';
import { Button } from '../../../../components/common/Button';
import { Card, CardContent } from '../../../../components/common/Card';

export function ActiveRestaurants({ restaurants, onSelect, onSuspend }) {
    return (
        <div className="restaurant-list">
            {restaurants.map((restaurant) => (
                <Card key={restaurant.id}>
                    <CardContent className="restaurant-card-content">
                        <img
                            src={restaurant.imageUrl || "https://placehold.co/150x150/e2e8f0/64748b?text=No+Image"}
                            alt={restaurant.name}
                            className="restaurant-image"
                        />
                        <div className="restaurant-info-wrapper">
                            <div className="restaurant-header">
                                <div>
                                    <h4 className="restaurant-name">{restaurant.name}</h4>
                                    <p className="restaurant-cuisine">{restaurant.description || "No description"}</p>
                                </div>
                                <Badge status="active">Active</Badge>
                            </div>
                            <div className="restaurant-stats">
                                <span>Owner: {restaurant.ownerName}</span>
                                <span className="separator">•</span>
                                <span>Phone number: {restaurant.phoneNumber || "N/A"}</span>
                            </div>
                            <div className="action-buttons">
                                <Button
                                    className="btn-outline"
                                    onClick={() => onSelect(restaurant)}
                                >
                                    View Details
                                </Button>
                                <Button
                                    className="btn-danger"
                                    onClick={() => onSuspend(restaurant.id)}
                                >
                                    Suspend
                                </Button>
                            </div>
                        </div>
                    </CardContent>
                </Card>
            ))}
        </div>
    );
}