import { Wifi, WifiOff } from 'lucide-react';
import { clsx } from 'clsx';

export default function ConnectionStatus({ connected = true }) {
  return (
    <div
      className={clsx(
        'inline-flex items-center gap-2 px-4 py-2 rounded-full shadow-lg',
        connected
          ? 'bg-emerald-50 text-emerald-600'
          : 'bg-amber-50 text-amber-600'
      )}
    >
      {connected ? (
        <>
          <div className="w-2 h-2 bg-emerald-500 rounded-full animate-pulse"></div>
          <Wifi size={16} />
          <span className="text-sm font-medium">Live Connection</span>
        </>
      ) : (
        <>
          <div className="w-2 h-2 bg-amber-500 rounded-full animate-pulse"></div>
          <WifiOff size={16} />
          <span className="text-sm font-medium">Reconnecting...</span>
        </>
      )}
    </div>
  );
}
