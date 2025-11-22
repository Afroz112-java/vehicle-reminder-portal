
import React from "react";
import { Bell, Clock, AlertTriangle } from "lucide-react";
import AppLayout from "../components/AppLayout";

export default function Reminders() {
  const reminders = [
    {
      id: 1,
      vehicle: "AP 09 AB 1234",
      type: "Insurance Renewal",
      date: "2025-01-15",
      status: "upcoming",
    },
    {
      id: 2,
      vehicle: "TS 10 CD 5678",
      type: "PUC Expiry",
      date: "2024-11-25",
      status: "expired",
    },
    {
      id: 3,
      vehicle: "AP 29 XY 9876",
      type: "Service Reminder",
      date: "2025-02-10",
      status: "upcoming",
    },
  ];

  return (
    <AppLayout>
      <div className="min-h-screen bg-gray-100 p-6">
        <h1 className="text-3xl font-bold mb-6 flex items-center gap-2">
          <Bell className="text-blue-600" /> Reminders
        </h1>

        {/* Container */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Upcoming Reminders */}
          <div className="bg-white shadow-md p-5 rounded-xl border">
            <h2 className="text-xl font-semibold mb-4 flex items-center gap-2">
              <Clock className="text-green-600" /> Upcoming Reminders
            </h2>

            {reminders.filter((r) => r.status === "upcoming").length === 0 ? (
              <p className="text-gray-500">No upcoming reminders.</p>
            ) : (
              reminders
                .filter((r) => r.status === "upcoming")
                .map((reminder) => (
                  <div
                    key={reminder.id}
                    className="p-3 mb-3 border rounded-lg bg-green-50 flex justify-between items-center"
                  >
                    <div>
                      <p className="font-semibold">{reminder.type}</p>
                      <p className="text-sm text-gray-600">
                        {reminder.vehicle}
                      </p>
                      <p className="text-sm text-green-700">
                        Date: {reminder.date}
                      </p>
                    </div>
                  </div>
                ))
            )}
          </div>

          {/* Expired Reminders */}
          <div className="bg-white shadow-md p-5 rounded-xl border">
            <h2 className="text-xl font-semibold mb-4 flex items-center gap-2">
              <AlertTriangle className="text-red-600" /> Expired Reminders
            </h2>

            {reminders.filter((r) => r.status === "expired").length === 0 ? (
              <p className="text-gray-500">No expired reminders.</p>
            ) : (
              reminders
                .filter((r) => r.status === "expired")
                .map((reminder) => (
                  <div
                    key={reminder.id}
                    className="p-3 mb-3 border rounded-lg bg-red-50 flex justify-between items-center"
                  >
                    <div>
                      <p className="font-semibold text-red-700">
                        {reminder.type}
                      </p>
                      <p className="text-sm text-gray-600">
                        {reminder.vehicle}
                      </p>
                      <p className="text-sm text-red-700">
                        Date: {reminder.date}
                      </p>
                    </div>
                  </div>
                ))
            )}
          </div>
        </div>
      </div>
    </AppLayout>
  );
}

