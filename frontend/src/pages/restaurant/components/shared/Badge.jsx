import { clsx } from 'clsx';

export default function Badge({ status }) {
  const variants = {
    'Đang bán': 'bg-orange-50 text-orange-600',
    'Hết hàng': 'bg-slate-100 text-slate-500'
  };

  return (
    <span
      className={clsx(
        'inline-flex items-center px-3 py-1 rounded-full text-sm',
        variants[status] || variants['Hết hàng']
      )}
    >
      {status}
    </span>
  );
}
