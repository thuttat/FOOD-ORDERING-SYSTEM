export default function AddItemModal({ onClose, onSave }) {
  const [formData, setFormData] = useState({ name: '', price: '', status: 'Available' });

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!formData.name || !formData.price) return alert("Duy nhập thiếu kìa!");
    onSave(formData);
  };

  return (
    <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div className="bg-white rounded-[32px] w-full max-w-md p-8 shadow-2xl animate-in zoom-in duration-200">
        <h2 className="text-2xl font-black mb-6">Thêm món mới 🍔</h2>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-bold text-slate-700 mb-2">Tên món ăn</label>
            <input 
              className="w-full bg-slate-50 border-none rounded-2xl px-4 py-3 focus:ring-2 focus:ring-emerald-500 transition-all"
              placeholder="Ví dụ: Burger Tôm..."
              onChange={(e) => setFormData({...formData, name: e.target.value})}
            />
          </div>
          
          <div>
            <label className="block text-sm font-bold text-slate-700 mb-2">Giá bán (VNĐ)</label>
            <input 
              type="number"
              className="w-full bg-slate-50 border-none rounded-2xl px-4 py-3 focus:ring-2 focus:ring-emerald-500 transition-all"
              placeholder="65000"
              onChange={(e) => setFormData({...formData, price: e.target.value + 'đ'})}
            />
          </div>

          <div className="flex gap-3 mt-8">
            <button 
              type="button" 
              onClick={onClose}
              className="flex-1 px-6 py-3 font-bold text-slate-500 hover:bg-slate-100 rounded-2xl transition-colors"
            >
              Hủy
            </button>
            <button 
              type="submit"
              className="flex-1 px-6 py-3 font-bold text-white bg-emerald-500 rounded-2xl shadow-lg shadow-emerald-200 transition-all active:scale-95"
            >
              Lưu món ăn
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}