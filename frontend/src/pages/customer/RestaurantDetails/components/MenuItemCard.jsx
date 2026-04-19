import React from "react";
import { Button } from "../../../../components/common/Button.jsx";

export function MenuItemCard({ item, onAdd }) {
    return (
        <div className="menu-item-card">
            <div className="item-img">
                <img src={item.imageUrl || "https://via.placeholder.com/150"} alt={item.name} />
            </div>
            <div className="item-details">
                <h4>{item.name}</h4>
                <p className="item-desc text-muted">{item.description}</p>
                <div className="item-footer">
                    <span className="price">{item.price.toLocaleString()}đ</span>
                    <Button 
                        className="btn-primary" 
                        onClick={() => onAdd(item.id)}
                        disabled={!item.available}
                    >
                        {item.available ? "Add" : "Sold out"}
                    </Button>
                </div>
            </div>
        </div>
    );
}