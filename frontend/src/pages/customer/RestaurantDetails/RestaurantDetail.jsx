import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axiosClient from "../../../apis/AxiosClient";
import { CartService } from "../../../apis/CartService.js";
import { MenuCategory } from "./components/MenuCategory.jsx";
import "./RestaurantDetail.css";

export function RestaurantDetail() {
    const { id } = useParams();
    const [restaurant, setRestaurant] = useState(null);
    const [menuGroups, setMenuGroups] = useState({});

    useEffect(() => {
        axiosClient.get(`/api/restaurants/${id}`)
            .then(res => {
                setRestaurant(res.data);
                const groups = res.data.menuItems.reduce((acc, item) => {
                    const catName = item.categoryName || "Other";
                    if (!acc[catName]) acc[catName] = [];
                    acc[catName].push(item);
                    return acc;
                }, {});
                setMenuGroups(groups);
            })
            .catch(err => console.error("Error loading menu:", err));
    }, [id]);

    const handleAddToCart = async (menuItemId) => {
        try {
            await CartService.addToCart(menuItemId, 1);
            alert("Successfully adding!");
        } catch (error) {
            console.error(error);
            alert("Cannot add to cart.");
        }
    };

    if (!restaurant) return <div className="loading">Loading...</div>;

    return (
        <div className="restaurant-detail">
            <div className="res-banner">
                <div className="res-info">
                    <h1>{restaurant.name}</h1>
                    <p>{restaurant.address}</p>
                    <div className="res-meta">
                        <span>⭐ {restaurant.rating || "5.0"}</span>
                        <span>•</span>
                        <span>{restaurant.description}</span>
                    </div>
                </div>
            </div>

            <div className="menu-container">
                {Object.keys(menuGroups).map(catName => (
                    <MenuCategory 
                        key={catName} 
                        title={catName} 
                        items={menuGroups[catName]} 
                        onAdd={handleAddToCart}
                    />
                ))}
            </div>
        </div>
    );
}