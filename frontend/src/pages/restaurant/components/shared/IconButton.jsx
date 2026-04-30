import { clsx } from 'clsx';

export default function IconButton({ icon: Icon, variant = 'edit', onClick }) {
  const variants = {
    edit: 'bg-blue-500 text-white shadow-md shadow-blue-200 hover:bg-blue-600 hover:shadow-blue-300',
    delete: 'bg-red-500 text-white shadow-md shadow-red-200 hover:bg-red-600 hover:shadow-red-300'
  };

  return (
    <button
      onClick={onClick}
      className={clsx(
        'p-2.5 rounded-full transition-all duration-200',
        variants[variant]
      )}
    >
      <Icon size={18} />
    </button>
  );
}
