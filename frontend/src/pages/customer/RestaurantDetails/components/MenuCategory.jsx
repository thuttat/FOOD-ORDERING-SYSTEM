import React from "react";
import { MenuItemCard } from "./MenuItemCard.jsx";

export function MenuCategory({ title, items, onAdd }) {
    if (!items || items.length === 0) return null;

    return (
        <section className="menu-category" style={{ marginBottom: '40px' }}>
            <h2 style={{
                borderBottom: '2px solid #f1f5f9',
                paddingBottom: '12px',
                marginBottom: '24px',
                color: '#0f172a',
                fontSize: '1.5rem'
            }}>
                {title}
            </h2>

            <div style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fill, minmax(350px, 1fr))',
                gap: '24px'
            }}>
                {items.map(item => (
                    <MenuItemCard key={item.id} item={item} onAdd={onAdd} />
                ))}
            </div>
        </section>
    );
}