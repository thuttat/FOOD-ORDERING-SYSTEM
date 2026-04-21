import React from 'react';
import { Plus } from 'lucide-react';


import MenuTable from './components/MenuTable';

export default function DashboardRestaurants() {

  const handleEdit = (item) => {
    console.log("Đang sửa món:", item);
    alert(`Bạn muốn sửa món: ${item.name}`);
    
  };

  const handleDelete = (item) => {
    const confirmDelete = window.confirm(`Bạn có chắc chắn muốn xóa món "${item.name}" không?`);
    if (confirmDelete) {
      console.log("Đang xóa món có ID:", item.id);
      
    }
  };

  return (
    <div className="p-8 space-y-6">
      <div className="flex justify-between items-center">
        <div>
           <h1 className="text-2xl font-bold text-slate-800 tracking-tight">MENU MANAGEMENT</h1>
           <p className="text-slate-500 text-sm">Update and edit your menu</p>
        </div>

        <button className="flex items-center gap-2 px-6 py-3 bg-teal-500 text-white rounded-xl hover:bg-teal-600 transition-all shadow-lg shadow-teal-200/50 active:scale-95 font-bold border-none outline-none">
          <Plus size={20} />
          <span>Add new items</span>
        </button>
      </div>

      <div className="bg-white rounded-[24px] shadow-xl shadow-slate-200/40 overflow-hidden">
        
        <MenuTable onEdit={handleEdit} onDelete={handleDelete} />
      </div>
    </div>
  );
}