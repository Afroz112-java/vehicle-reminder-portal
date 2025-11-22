
import React from "react";
import AppLayout from "../components/AppLayout";
import { Bell, Car, AlertTriangle, CalendarDays } from "lucide-react";

export default function Dashboard() {
  const stats = [
    { id: 1, label: "Total Vehicles", value: 12, icon: Car, color: "text-blue-600" },
    { id: 2, label: "Upcoming Reminders", value: 5, icon: Bell, color: "text-green-600" },
    { id: 3, label: "Expired Reminders", value: 2, icon: AlertTriangle, color: "text-red-600" },
  ];

  const upcoming = [
    { id: 1, vehicle: "AP 09 AB 1234", task: "Insurance Renewal", date: "2025-01-15" },
    { id: 2, vehicle: "TS 10 CD 5678", task: "PUC Expiry", date: "2024-12-01" },
  ];

  return (
    <AppLayout>
      <div className="p-6 bg-gray-100 min-h-screen">
        
        {/* Header */}
        <h1 className="text-3xl font-bold mb-6">Dashboard</h1>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 mb-8">
          {stats.map((item) => {
            const Icon = item.icon;
            return (
              <div
                key={item.id}
                className="bg-white shadow p-5 rounded-xl flex items-center gap-4 border"
              >
                <div className={`p-3 rounded-full ${item.color} bg-gray-100`}>
                  <Icon size={26} />
                </div>
                <div>
                  <p className="text-gray-600">{item.label}</p>
                  <p className="text-2xl font-bold">{item.value}</p>
                </div>
              </div>
            );
          })}
        </div>

        {/* Two-Column Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">

          {/* Upcoming Reminders */}
          <div className="bg-white rounded-xl border shadow p-5">
            <h2 className="text-xl font-semibold mb-4 flex items-center gap-2">
              <CalendarDays className="text-blue-600" /> Upcoming Reminders
            </h2>

            {upcoming.length === 0 ? (
              <p className="text-gray-500">No upcoming reminders.</p>
            ) : (
              upcoming.map((rem) => (
                <div
                  key={rem.id}
                  className="p-3 mb-3 border rounded-lg bg-blue-50"
                >
                  <p className="font-semibold">{rem.task}</p>
                  <p className="text-sm text-gray-600">{rem.vehicle}</p>
                  <p className="text-sm text-blue-700">Date: {rem.date}</p>
                </div>
              ))
            )}
          </div>

          {/* Vehicles Overview */}
          <div className="bg-white rounded-xl border shadow p-5">
            <h2 className="text-xl font-semibold mb-4 flex items-center gap-2">
              <Car className="text-indigo-600" /> Vehicles Overview
            </h2>

            <ul className="space-y-3">
              <li className="p-3 bg-gray-50 border rounded-lg">
                Honda Activa – Insurance due in 30 days
              </li>
              <li className="p-3 bg-gray-50 border rounded-lg">
                Hyundai i20 – Insurance due in 45 days
              </li>
              <li className="p-3 bg-gray-50 border rounded-lg">
                Royal Enfield – Service upcoming this month
              </li>
            </ul>
          </div>

        </div>
      </div>
    </AppLayout>
  );
}
