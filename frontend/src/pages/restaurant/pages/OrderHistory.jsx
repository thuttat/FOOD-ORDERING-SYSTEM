import React, { useEffect, useState } from 'react';
import axiosClient from '../../../apis/AxiosClient';
import OrderTable from '../../restaurant/components/orders/OrderTable'; 

export default function OrderHistory() {
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchHistory = async () => {
      try {
        const res = await axiosClient.get("/orders/history");
        setHistory(Array.isArray(res) ? res : res.data || []);
      } catch (err) {
        console.error("Failed to fetch history", err);
      } finally {
        setLoading(false);
      }
    };
    fetchHistory();
  }, []);

  return (
    <div className="bg-white rounded-3xl shadow-sm p-6">
      <h2 className="text-2xl font-bold text-slate-800 mb-6">Order History</h2>
      <OrderTable history={history} loading={loading} />
    </div>
  );
}