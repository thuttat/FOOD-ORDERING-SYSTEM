import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';

export default function AddItemModal({ onClose, onSubmit, initialData }) {
  const [name, setName] = useState(initialData?.name || '');
  const [price, setPrice] = useState(initialData?.price || '');
  const [description, setDescription] = useState(initialData?.description || '');
  const [categoryName, setCategoryName] = useState(initialData?.categoryName || '');
  const [file, setFile] = useState(null);
  const modalTitle = initialData ? "Edit Dish" : "Add New Dish";

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!categoryName) {
      alert("Please enter a category name!");
      return;
    }

    onSubmit({
      name,
      price,
      description,
      categoryName,
      image: file
    });
  };

  const handleFileChange = (e) => {
    const selected = e.target.files[0];
    if (selected) {
      setFile(selected);
    }
  };

  useEffect(() => {
    if (initialData) {
      setName(initialData.name || '');
      setCategoryName(initialData.categoryName || '');
      setPrice(initialData.price || '');
      setDescription(initialData.description || '');
    }
  }, [initialData]);

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/40 backdrop-blur-sm p-4">
      <div className="bg-white w-full max-w-md rounded-[32px] p-8 shadow-2xl animate-in zoom-in duration-200 relative">

        <button
          onClick={onClose}
          className="absolute top-6 right-6 text-slate-400 hover:text-slate-600 transition-colors"
        >
          <X size={24} />
        </button>

        <h2 className="text-2xl font-black text-slate-800 mb-6">
          {initialData ? "Edit Dish" : "Add New Dish"}
        </h2>

        <div className="bg-white w-full max-w-md rounded-[32px] p-8 shadow-2xl animate-in zoom-in duration-200 relative overflow-y-auto max-h-[90vh]">
          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label className="block text-sm font-bold text-slate-700 mb-2">Item Name</label>
              <input
                type="text"
                required
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="e.g. Classic Beef Burger"
                className="w-full px-5 py-3 bg-slate-50 border-none rounded-2xl focus:ring-2 focus:ring-teal-500 transition-all outline-none"
              />
            </div>

            <div>
              <label className="block text-sm font-bold text-slate-700 mb-2">Category</label>
              <input
                type="text"
                required
                value={categoryName}
                onChange={(e) => setCategoryName(e.target.value)}
                placeholder="e.g. Lẩu, Món gà, Hải sản..."
                className="w-full px-5 py-3 bg-slate-50 border-none rounded-2xl focus:ring-2 focus:ring-teal-500 transition-all outline-none"
              />
            </div>

            <div>
              <label className="block text-sm font-bold text-slate-700 mb-2">Price (VND)</label>
              <input
                type="number"
                required
                value={price}
                onChange={(e) => setPrice(e.target.value)}
                placeholder="e.g. 50000"
                className="w-full px-5 py-3 bg-slate-50 border-none rounded-2xl focus:ring-2 focus:ring-teal-500 transition-all outline-none"
              />
            </div>

            <div>
              <label className="block text-sm font-bold text-slate-700 mb-2">Description</label>
              <textarea
                rows="3"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Describe your delicious dish..."
                className="w-full px-5 py-3 bg-slate-50 border-none rounded-2xl focus:ring-2 focus:ring-teal-500 transition-all outline-none resize-none"
              />
            </div>

            <div className="space-y-2">
              <label className="block text-sm font-bold text-slate-700 mb-2">
                Upload Image
              </label>

              <input
                type="file"
                accept="image/*"
                onChange={handleFileChange}
                className="w-full px-4 py-2 border-2 border-dashed border-slate-200 rounded-2xl text-sm file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-teal-50 file:text-teal-700 hover:file:bg-teal-100 transition-all"
              />

              {initialData?.imageUrl && !file && (
                <div className="mt-3 p-3 bg-slate-50 rounded-2xl border border-slate-100 flex items-center gap-3">
                  <div className="text-[10px] uppercase font-bold text-slate-400 tracking-wider">
                    Current image:
                  </div>
                  <img
                    src={initialData.imageUrl}
                    alt="Current"
                    className="w-12 h-12 object-cover rounded-xl shadow-sm border border-white"
                  />
                </div>
              )}
            </div>

            <div className="flex gap-3 pt-4">
              <button
                type="button"
                onClick={onClose}
                className="flex-1 px-6 py-3 font-bold text-slate-500 hover:bg-slate-100 rounded-xl transition-all"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="flex-1 px-6 py-3 bg-teal-500 text-white font-bold rounded-xl shadow-lg shadow-teal-200 hover:bg-teal-600 transition-all active:scale-95"
              >
                Confirm & Save
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}