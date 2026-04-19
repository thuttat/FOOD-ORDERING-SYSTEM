import React from 'react';
import { Trash2, Plus, Minus } from 'lucide-react';

export function CartItem({ item, onUpdate, onRemove }) {
    return (
        <div className="cart-item-card">
            <div className="cart-item-image">
                <img src={item.imageUrl || 'https://via.placeholder.com/100'} alt={item.name} />
            </div>
            
            <div className="cart-item-info">
                <h4>{item.name}</h4>
                <p className="item-price">{item.price?.toLocaleString()}đ</p>
            </div>

            <div className="cart-item-actions">
                <div className="quantity-toggle">
                    <button 
                        onClick={() => onUpdate(item.id, item.quantity - 1)}
                        className="qty-btn"
                    >
                        <Minus size={16} />
                    </button>
                    <span className="qty-value">{item.quantity}</span>
                    <button 
                        onClick={() => onUpdate(item.id, item.quantity + 1)}
                        className="qty-btn"
                    >
                        <Plus size={16} />
                    </button>
                </div>
                
                <button 
                    className="delete-item-btn"
                    onClick={() => onRemove(item.id)}
                >
                    <Trash2 size={20} />
                </button>
            </div>

            <div className="cart-item-total">
                {(item.price * item.quantity).toLocaleString()}đ
            </div>
        </div>
    );
}