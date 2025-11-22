
import React from "react";
import AppLayout from "../components/AppLayout";
import { ShieldCheck, AlertTriangle, Clock } from "lucide-react";

export default function InsuranceStatus() {
  const insuranceData = [
    {
      id: 1,
      vehicle: "AP 09 AB 1234",
      model: "Honda Activa",
      expiry: "2025-01-15",
      status: "active",
    },
    {
      id: 2,
      vehicle: "TS 10 CD 5678",
      model: "Hyundai i20",
      expiry: "2024-12-05",
      status: "expiring",
    },
    {
      id: 3,
      vehicle: "AP 29 XY 9987",
      model: "Royal Enfield Classic",
      expiry: "2024-10-20",
      status: "expired",
    },
  ];

  return (
    <AppLayout>
      <div className="p-6 bg-gray-100 min-h-screen">
        <h1 className="text-3xl font-bold mb-6">Insurance Status</h1>

        {/* Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">

          {insuranceData.map((item) => (
            <div
              key={item.id}
              className={`p-5 rounded-xl shadow border bg-white 
              ${item.status === "active" ? "border-green-500" : ""}
              ${item.status === "expiring" ? "border-yellow-500" : ""}
              ${item.status === "expired" ? "border-red-500" : ""}`}
            >
              {/* Header */}
              <div className="flex items-center gap-3 mb-3">
                {item.status === "active" && (
                  <ShieldCheck className="text-green-600" size={25} />
                )}
                {item.status === "expiring" && (
                  <Clock className="text-yellow-600" size={25} />
                )}
                {item.status === "expired" && (
                  <AlertTriangle className="text-red-600" size={25} />
                )}

                <h2 className="text-xl font-semibold">{item.vehicle}</h2>
              </div>

              {/* Model */}
              <p className="text-gray-700 mb-1 text-sm">
                <span className="font-semibold">Model:</span> {item.model}
              </p>

              {/* Expiry */}
              <p className="text-gray-700 text-sm">
                <span className="font-semibold">Insurance Expiry:</span>{" "}
                {item.expiry}
              </p>

              {/* Status Badge */}
              <div className="mt-4">
                {item.status === "active" && (
                  <span className="px-3 py-1 bg-green-100 text-green-700 rounded-lg text-sm font-medium">
                    Active
                  </span>
                )}

                {item.status === "expiring" && (
                  <span className="px-3 py-1 bg-yellow-100 text-yellow-700 rounded-lg text-sm font-medium">
                    Expiring Soon
                  </span>
                )}

                {item.status === "expired" && (
                  <span className="px-3 py-1 bg-red-100 text-red-700 rounded-lg text-sm font-medium">
                    Expired
                  </span>
                )}
              </div>
            </div>
          ))}

        </div>
      </div>
    </AppLayout>
  );
}
