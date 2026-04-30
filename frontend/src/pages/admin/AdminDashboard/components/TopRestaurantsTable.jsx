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
                        </tr>
                        </thead>

                        <tbody>
                        {data.map((restaurant, index) => (
                            <tr key={index}>
                                <td>{restaurant.name}</td>
                                <td>{restaurant.orderCount}</td>
                                <td>{restaurant.revenue.toLocaleString()} ₫</td>
                                <td>{restaurant.rating.toFixed(1)}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </CardContent>
        </Card>
    );
}