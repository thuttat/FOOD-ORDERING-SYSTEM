import { Pencil, Trash2 } from 'lucide-react';
import Badge from '../shared/Badge';
import IconButton from '../shared/IconButton'
import { clsx } from 'clsx';
import { useState, useEffect } from 'react';
import axiosClient from '../../../../apis/AxiosClient.js';


export default function MenuTable({ onEdit, onDelete, onToggle }) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchMenuItems = async () => {
    try {
      const res = await axiosClient.get("/menu-items");
      setItems(Array.isArray(res) ? res : res.data || []);
    } catch (error) {
      console.error("Error:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchMenuItems(); }, []);

  if (loading) return <div className="p-8 text-center">Loading...</div>;

  return (
    <table className="w-full">
      <thead>
        <tr className="border-b border-slate-100">
          <th className="text-left px-8 py-4 text-slate-600 w-24">Image</th>
          <th className="text-left px-8 py-4 text-slate-600">Name</th>
          <th className="text-left px-8 py-4 text-slate-600">Category</th>
          <th className="text-left px-8 py-4 text-slate-600">Price</th>
          <th className="text-left px-8 py-4 text-slate-600">Status</th>
          <th className="text-center px-8 py-4 text-slate-600">Actions</th>
        </tr>
      </thead>
      <tbody>
        {items.length > 0 ? items.map((item) => (
          <tr key={item.id} className="border-b border-slate-50 hover:bg-slate-50/50">
            <td className="px-8 py-4">
              <div className="w-16 h-16 rounded-full overflow-hidden border border-slate-200">
                <img src={item.imageUrl || "https://via.placeholder.com/150"} className="w-full h-full object-cover" />
              </div>
            </td>
            <td className="px-8 py-6 font-bold">{item.name}</td>
            <td className="px-8 py-6 text-slate-400">{item.categoryName}</td>
            <td className="px-8 py-6">{item.price?.toLocaleString()}VND</td>
            <td className="px-8 py-6">
              <button
                onClick={() => onToggle(item)} 
                className="focus:outline-none active:scale-95 transition-transform"
                title="Click to toggle availability"
              >
                <Badge status={item.isAvailable ? 'Available' : 'Sold Out'} />
              </button>
            </td>
            <td className="px-8 py-6">
              <div className="flex gap-2 justify-center">
                <IconButton icon={Pencil} variant="edit" onClick={() => onEdit(item)} />
                <IconButton icon={Trash2} variant="delete" onClick={() => onDelete(item)} />
              </div>
            </td>
          </tr>
        )) : (
          <tr><td colSpan="6" className="py-20 text-center text-slate-400">Menu is empty</td></tr>
        )}
      </tbody>
    </table>
  );
}