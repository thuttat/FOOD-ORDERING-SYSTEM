import React from "react";
import { Card, CardContent } from "../../../../components/common/Card.jsx";

export function TopRestaurantsTable({ data }) {
    return (
        <Card>
            <CardContent className="table-card">
                <h3>Top Restaurants</h3>

                <div className="table-wrapper">
                    <table className="table">
                        <thead>
                        <tr>
                            <th>Restaurant</th>
                            <th>Orders</th>
                            <th>Revenue</th>
                            <th>Rating</th>
                            <th>Status</th>
                        </tr>
                        </thead>

                        <tbody>
                        {data.map((restaurant, index) => (
                            <tr key={index}>
                                <td>{restaurant.name}</td>
                                <td>{restaurant.orders}</td>
                                <td>${restaurant.revenue}</td>
                                <td>{restaurant.rating}</td>
                                <td>
                    <span className="status-badge">
                      {restaurant.status}
                    </span>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </CardContent>
        </Card>
    );
}