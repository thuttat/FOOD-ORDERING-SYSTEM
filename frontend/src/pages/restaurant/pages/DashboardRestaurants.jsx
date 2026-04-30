import React, { useEffect, useState } from 'react';
import { Plus } from 'lucide-react';
import axiosClient from '../../../apis/AxiosClient.js';
import MenuTable from '../components/menu/MenuTable';
import AddItemModal from '../components/menu/AddItemModal';
import MenuHeader from '../components/menu/MenuHeader';

export default function DashboardRestaurants() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [refreshKey, setRefreshKey] = useState(0);
  const [editingItem, setEditingItem] = useState(null);

  const handleOpenAdd = () => { setEditingItem(null); setIsModalOpen(true); };
  const handleOpenEdit = (item) => { setEditingItem(item); setIsModalOpen(true); };
  const handleCloseModal = () => { setIsModalOpen(false); setEditingItem(null); };
  const handleDelete = async (item) => {
    if (!window.confirm(`Are you sure you want to delete "${item.name}"?`)) return;

    try {
      await axiosClient.delete(`/menu-items/${item.id}`);
      alert("Item deleted successfully!");
      setRefreshKey(prev => prev + 1);
    } catch (error) {
      alert("Delete failed: " + (error.response?.data?.message || "Error"));
    }
  };


  const handleSubmit = async (formData) => {
    try {
      const dataForServer = new FormData();
      const menuData = {
        name: formData.name,
        description: formData.description || "No description provided",
        price: parseFloat(formData.price),
        categoryName: formData.categoryName,
        isAvailable: true
      };

      dataForServer.append("data", new Blob([JSON.stringify(menuData)], {
        type: "application/json"
      }));

      if (formData.image) {
        dataForServer.append("image", formData.image);
      }

      if (editingItem) {
        await axiosClient.put(`/menu-items/${editingItem.id}`, dataForServer, {
          headers: { "Content-Type": "multipart/form-data" }
        });
      } else {
        if (!formData.image) return alert("Please select an image!");
        await axiosClient.post("/menu-items", dataForServer, {
          headers: { "Content-Type": "multipart/form-data" }
        });
      }

      alert(editingItem ? "Update successfully!" : "Add item successfully!");
      handleCloseModal();
      setRefreshKey(prev => prev + 1);
    } catch (error) {
      console.error("Detailed error:", error.response?.data);
      alert("Failed: " + (error.response?.data?.message || "System error"));
    }
  };

  const handleToggleStatus = async (item) => {
    try {
      await axiosClient.put(`/menu-items/${item.id}/toggle`);
      setRefreshKey(prev => prev + 1);
    } catch (error) {
      alert("Toggle failed!");
    }
  };

  return (
    <div className="p-8 space-y-6 bg-slate-50 min-h-screen">
      <MenuHeader onAddClick={handleOpenAdd} />

      <div className="bg-white rounded-[32px] shadow-sm border border-slate-100 overflow-hidden">
        <MenuTable
          key={refreshKey}
          onEdit={handleOpenEdit}
          onDelete={handleDelete}
          onToggle={handleToggleStatus}
        />
      </div>

      {isModalOpen && (
        <AddItemModal
          onClose={handleCloseModal}
          onSubmit={handleSubmit}
          initialData={editingItem}
        />
      )}

    </div>
  );
}