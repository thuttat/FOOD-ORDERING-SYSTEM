import { Edit2, Pencil, Trash2 } from 'lucide-react';
import Badge from './Badge';
import IconButton from './IconButton'
import { clsx } from 'clsx';

export default function MenuTable() {
  const menuItems = [
    { name: 'Classic Burger', price: '65.000đ', status: 'Available', highlight: true },
    { name: 'Cheese Pizza', price: '120.000đ', status: 'Sold Out', highlight: false },
    { name: 'Design Apuurion', price: '175.000đ', status: 'Sold Out', highlight: false }
  ];

  return (
    <div className="bg-white rounded-3xl shadow-xl shadow-slate-200/50 overflow-hidden">
      <table className="w-full">
        <thead>
          <tr className="border-b border-slate-100">
            <th className="text-left px-8 py-4 text-slate-600 font-semibold">Name</th>
            <th className="text-left px-8 py-4 text-slate-600 font-semibold">Price</th>
            <th className="text-left px-8 py-4 text-slate-600 font-semibold">Status</th>
            <th className="text-left px-8 py-4 text-slate-600 font-semibold">Actions</th>
          </tr>
        </thead>
        <tbody>
          {menuItems.map((item, index) => (
            <tr
              key={index}
              className={clsx(
                'border-b border-slate-50 transition-colors hover:bg-slate-50/50',
                item.highlight && 'bg-orange-50/50'
              )}
            >
              <td className="px-8 py-6">
                <span className="font-bold text-slate-900">{item.name}</span>
              </td>
              <td className="px-8 py-6">
                <span className="text-slate-700">{item.price}</span>
              </td>
              <td className="px-8 py-6">
                <Badge status={item.status} />
              </td>
              <td className="px-8 py-6">
                <div className="flex gap-2">
                  
                  <IconButton
                    icon={Pencil}
                    variant="edit"
                    onClick={() => handleEdit(item)}
                  />

                  <IconButton
                    icon={Trash2}
                    variant="delete"
                    onClick={() => handleDelete(item)}
                  />
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
