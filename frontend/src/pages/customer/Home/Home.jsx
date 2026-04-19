import React, { useEffect, useState } from "react";
import axiosClient from "../../api/AxiosClient";
import { Card, CardContent } from "../../components/common/Card.jsx";
import { Button } from "../../components/common/Button.jsx";
import "./Home.css";

export function Home() {
    const [restaurants, setRestaurants] = useState([]);

    useEffect(() => {
        axiosClient.get("/api/restaurants")
            .then(res => setRestaurants(res.data))
            .catch(err => console.error(err));
    }, []);

    return (
        <div className="home-container">
            <h1 className="page-title">Explore us</h1>
            <div className="restaurant-grid">
                {restaurants.map(res => (
                    <Card key={res.id} hover className="restaurant-card">
                        <CardContent>
                            <h3>{res.name}</h3>
                            <p className="text-muted">{res.address}</p>
                            <Button className="btn-primary" onClick={() => window.location.href=`/restaurant/${res.id}`}>
                                Menu
                            </Button>
                        </CardContent>
                    </Card>
                ))}
            </div>
        </div>
    );
}