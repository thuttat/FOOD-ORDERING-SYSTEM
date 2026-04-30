import React from 'react';
import { Plus } from 'lucide-react';

export default function MenuHeader({ onAddClick }) {
  return (
    <div className="flex justify-between items-center">
      <div>
        <h1 className="text-3xl font-black text-slate-800 tracking-tight">MENU MANAGEMENT</h1>
        <p className="text-slate-500">Manage your restaurant's menu items</p>
      </div>
      <button
        onClick={onAddClick}
        className="flex items-center gap-2 px-6 py-3 bg-emerald-500 hover:bg-emerald-600 text-white rounded-2xl font-bold shadow-lg shadow-emerald-200 transition-all"
      >
        <Plus size={20} /> Add New Item
      </button>
    </div>
  );
}