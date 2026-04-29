import React from 'react';
import { Trash2, Plus, Minus } from 'lucide-react';

export function CartItem({ item, onUpdate, onRemove }) {
    const itemName = item.name || item.menuItemName || (item.menuItem && item.menuItem.name) || "N/A";
    const itemPrice = Number(item.price || item.unitPrice || (item.menuItem && item.menuItem.price)) || 0;
    const quantity = Number(item.quantity) || 0;

    return (
        <div className="cart-item-card">
            <div className="cart-item-image">
                <img src={item.imageUrl || (item.menuItem && item.menuItem.imageUrl) || 'https://via.placeholder.com/100'} alt={itemName} />
            </div>

            <div className="cart-item-info">
                <h4>{itemName}</h4>
                <p className="item-price">{itemPrice.toLocaleString()}đ</p>
            </div>

            <div className="cart-item-actions">
                <div className="quantity-toggle">
                    <button onClick={() => onUpdate(item.id, quantity - 1)} className="qty-btn">
                        <Minus size={16} />
                    </button>
                    <span className="qty-value">{quantity}</span>
                    <button onClick={() => onUpdate(item.id, quantity + 1)} className="qty-btn">
                        <Plus size={16} />
                    </button>
                </div>

                <button className="delete-item-btn" onClick={() => onRemove(item.id)}>
                    <Trash2 size={20} />
                </button>
            </div>

            <div className="cart-item-total">
                {(itemPrice * quantity).toLocaleString()}đ
            </div>
        </div>
    );
}