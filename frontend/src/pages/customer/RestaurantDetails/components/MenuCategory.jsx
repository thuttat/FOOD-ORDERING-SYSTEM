import React from "react";
import { MenuItemCard } from "./MenuItemCard.jsx";

export function MenuCategory({ title, items, onAdd }) {
    return (
        <section className="menu-category">
            <h2 className="category-title">{title}</h2>
            <div className="items-grid">
                {items.map(item => (
                    <MenuItemCard key={item.id} item={item} onAdd={onAdd} />
                ))}
            </div>
        </section>
    );
}