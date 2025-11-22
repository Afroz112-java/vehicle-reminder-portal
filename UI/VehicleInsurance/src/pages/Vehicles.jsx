
import React, { useState, useEffect } from "react";
import { Car, Bell, Trash, Pencil, Check, X } from "lucide-react";
import AppLayout from "../components/AppLayout";
import { useNavigate } from "react-router-dom";
import { toast } from "react-hot-toast";

export default function Vehicles() {
  const navigate = useNavigate();

  const [vehicles, setVehicles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editRowId, setEditRowId] = useState(null);
  const [editData, setEditData] = useState({});

  // 🔥 Fetch Vehicles
  useEffect(() => {
    const fetchVehicles = async () => {
      try {
        const token = localStorage.getItem("token");

        const res = await fetch("http://localhost:8081/api/vehicle", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!res.ok) {
          toast.error("Failed to fetch vehicles!");
          return;
        }

        const data = await res.json();
        setVehicles(data);
      } catch (error) {
        toast.error("Error fetching vehicles!");
      } finally {
        setLoading(false);
      }
    };

    fetchVehicles();
  }, []);

  // 📌 CSV Upload
  const handleCSVUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const token = localStorage.getItem("token");
    const formData = new FormData();
    formData.append("file", file);

    try {
      const res = await fetch("http://localhost:8081/api/vehicle/upload-csv", {
        method: "POST",
        headers: { Authorization: `Bearer ${token}` },
        body: formData,
      });

      if (res.ok) {
        toast.success("CSV Uploaded Successfully!");
        window.location.reload();
      } else {
        toast.error("CSV Upload Failed!");
      }
    } catch (err) {
      toast.error("Error uploading CSV!");
    }
  };

  // 📥 EXPORT CSV
  const exportCSV = async () => {
    const token = localStorage.getItem("token");

    try {
      const res = await fetch("http://localhost:8081/api/reminders/export", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!res.ok) {
        toast.error("Failed to export CSV!");
        return;
      }

      const blob = await res.blob();
      const url = window.URL.createObjectURL(blob);

      const a = document.createElement("a");
      a.href = url;
      a.download = "reminders_export.csv";
      a.click();

      toast.success("CSV Exported Successfully!");
      window.URL.revokeObjectURL(url);
    } catch (err) {
      toast.error("Error exporting CSV!");
    }
  };

  // 📌 Manual Reminder
  const sendManualReminder = async (vehicleId) => {
    const token = localStorage.getItem("token");

    try {
      const res = await fetch(
        `http://localhost:8081/api/reminders/send/${vehicleId}`,
        {
          method: "POST",
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      if (res.ok) {
        toast.success("Reminder Sent Successfully!");
      } else {
        toast.error("Failed to send reminder!");
      }
    } catch (err) {
      toast.error("Error sending reminder!");
    }
  };

  // 📌 Delete
  const deleteVehicle = async (vehicleId) => {
    const token = localStorage.getItem("token");

    if (!window.confirm("Are you sure you want to delete this vehicle?"))
      return;

    try {
      const res = await fetch(
        `http://localhost:8081/api/vehicle/${vehicleId}`,
        {
          method: "DELETE",
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      if (res.ok) {
        toast.success("Vehicle Deleted!");
        setVehicles((prev) => prev.filter((v) => v.id !== vehicleId));
      } else {
        toast.error("Failed to delete vehicle!");
      }
    } catch (err) {
      toast.error("Error deleting vehicle!");
    }
  };

  // 📌 Save Edit
  const saveEdit = async () => {
    const token = localStorage.getItem("token");

    try {
      const res = await fetch(
        `http://localhost:8081/api/vehicle/${editRowId}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(editData),
        }
      );

      if (res.ok) {
        toast.success("Vehicle Updated!");
        setVehicles((prev) =>
          prev.map((v) => (v.id === editRowId ? { ...v, ...editData } : v))
        );
        setEditRowId(null);
        setEditData({});
      } else {
        toast.error("Failed to update vehicle!");
      }
    } catch (err) {
      toast.error("Error updating vehicle!");
    }
  };
  

  // 📌 Status Badge Logic
  const getStatus = (insuranceDate) => {
    if (!insuranceDate) return "Unknown";

    const today = new Date();
    const expiry = new Date(insuranceDate);
    const diff = Math.ceil((expiry - today) / (1000 * 60 * 60 * 24));

    if (diff < 0) return "Expired";
    if (diff <= 30) return "Expiring";
    return "Active";
  };
  const [showPopup, setShowPopup] = useState(false);
  const [popupMessage, setPopupMessage] = useState("");
  const [popupAction, setPopupAction] = useState(null);

  const openPopup = (message, action) => {
    setPopupMessage(message);
    setPopupAction(() => action);
    setShowPopup(true);
  };

  const confirmPopup = () => {
    if (popupAction) popupAction();
    setShowPopup(false);
  };

  const getStatusClass = (status) => {
    switch (status) {
      case "Active":
        return "bg-green-100 text-green-700";
      case "Expiring":
        return "bg-yellow-100 text-yellow-700";
      case "Expired":
        return "bg-red-100 text-red-700";
      default:
        return "bg-gray-100 text-gray-700";
    }
  };

  return (
    <AppLayout>
      <div className="p-10">
        {/* HEADER */}
        <div className="flex justify-between items-center mb-6">
          <div>
            <h1 className="text-4xl font-bold">Vehicles</h1>
            <p className="text-gray-600">Manage all your registered vehicles</p>
          </div>

          <div className="flex gap-3">
            {/* Add Vehicle */}
            <button
              className="px-5 py-2 bg-blue-600 text-white rounded-lg"
              onClick={() => navigate("/vehicles/add-vehicle")}
            >
              <Car className="inline-block mr-2" size={18} />
              Add Vehicle
            </button>

            {/* Upload CSV */}
            <button
              className="px-5 py-2 bg-green-600 text-white rounded-lg"
              onClick={() => document.getElementById("csvUploadInput").click()}
            >
              Upload CSV
            </button>

            <input
              id="csvUploadInput"
              type="file"
              accept=".csv"
              className="hidden"
              onChange={handleCSVUpload}
            />

            {/* Export CSV */}
            <button
              className="px-5 py-2 bg-orange-600 text-white rounded-lg"
              onClick={exportCSV}
            >
              Export CSV
            </button>
          </div>
        </div>

        {/* TABLE */}
        <div className="bg-white shadow rounded-xl p-6">
          <h2 className="text-2xl font-semibold mb-6">All Vehicles</h2>

          {loading ? (
            <p>Loading...</p>
          ) : (
            <table className="w-full">
              <thead>
                <tr className="border-b">
                  <th className="py-3">Vehicle No</th>
                  <th className="py-3">Brand</th>
                  <th className="py-3">Model</th>
                  <th className="py-3">Insurance Expiry</th>
                  <th className="py-3">Status</th>
                  <th className="py-3">Actions</th>
                </tr>
              </thead>

              <tbody>
                {vehicles.map((v) => {
                  const status = getStatus(v.insuranceExpiryDate);

                  return (
                    <tr key={v.id} className="border-b">
                      {/* REG NUMBER */}
                      <td className="py-4">
                        {editRowId === v.id ? (
                          <input
                            className="border px-2 py-1"
                            value={editData.regNumber}
                            onChange={(e) =>
                              setEditData({
                                ...editData,
                                regNumber: e.target.value,
                              })
                            }
                          />
                        ) : (
                          <span className="text-blue-600">{v.regNumber}</span>
                        )}
                      </td>

                      {/* BRAND */}
                      <td className="py-4">
                        {editRowId === v.id ? (
                          <input
                            className="border px-2 py-1"
                            value={editData.brand}
                            onChange={(e) =>
                              setEditData({
                                ...editData,
                                brand: e.target.value,
                              })
                            }
                          />
                        ) : (
                          v.brand
                        )}
                      </td>

                      {/* MODEL */}
                      <td className="py-4">
                        {editRowId === v.id ? (
                          <input
                            className="border px-2 py-1"
                            value={editData.model}
                            onChange={(e) =>
                              setEditData({
                                ...editData,
                                model: e.target.value,
                              })
                            }
                          />
                        ) : (
                          v.model
                        )}
                      </td>

                      {/* INSURANCE */}
                      <td className="py-4">
                        {editRowId === v.id ? (
                          <input
                            type="date"
                            className="border px-2 py-1"
                            value={editData.insuranceExpiryDate}
                            onChange={(e) =>
                              setEditData({
                                ...editData,
                                insuranceExpiryDate: e.target.value,
                              })
                            }
                          />
                        ) : (
                          v.insuranceExpiryDate
                        )}
                      </td>

                      {/* STATUS */}
                      <td className="py-4">
                        <span
                          className={`px-3 py-1 rounded-full text-sm ${getStatusClass(
                            status
                          )}`}
                        >
                          {status}
                        </span>
                      </td>

                      {/* ACTIONS */}
                      <td className="py-4 flex gap-3">
                        {editRowId === v.id ? (
                          <>
                            <button
                              className="px-4 py-2 bg-green-600 text-white rounded-lg flex items-center gap-1"
                              onClick={() =>
                                openPopup(
                                  "Save changes to this vehicle?",
                                  saveEdit
                                )
                              }
                            >
                              <Check size={16} /> Save
                            </button>

                            <button
                              className="px-4 py-2 bg-gray-600 text-white rounded-lg flex items-center gap-1"
                              onClick={() => {
                                setEditRowId(null);
                                setEditData({});
                              }}
                            >
                              <X size={16} /> Cancel
                            </button>
                          </>
                        ) : (
                          <>
                            <button
                              className="px-3 py-2 bg-yellow-500 text-white rounded-lg"
                              onClick={() => {
                                setEditRowId(v.id);
                                setEditData({
                                  regNumber: v.regNumber,
                                  brand: v.brand,
                                  model: v.model,
                                  insuranceExpiryDate:
                                    v.insuranceExpiryDate,
                                });
                              }}
                            >
                              <Pencil size={16} />
                            </button>

                            <button
                              className="px-3 py-2 bg-purple-600 text-white rounded-lg"
                              onClick={() =>
                                openPopup(
                                  "Send reminder for this vehicle?",
                                  () => sendManualReminder(v.id)
                                )
                              }
                            >
                              <Bell size={16} />
                            </button>

                            <button
                              className="px-3 py-2 bg-red-600 text-white rounded-lg"
                              onClick={() =>
                                openPopup(
                                  "Are you sure you want to delete this vehicle?",
                                  () => deleteVehicle(v.id)
                                )
                              }
                            >
                              <Trash size={16} />
                            </button>
                          </>
                        )}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </div>
      </div>

      {/* POPUP CONFIRMATION */}
      {showPopup && (
        <div className="fixed inset-0 bg-transparent flex justify-center items-center z-50 backdrop-blur-sm">
          <div className="bg-white p-6 rounded-xl shadow-xl text-center w-96">
            <h2 className="text-lg font-semibold mb-4">{popupMessage}</h2>

            <div className="flex justify-center gap-4 mt-4">
              <button
                onClick={confirmPopup}
                className="px-5 py-2 bg-red-600 text-white rounded-lg"
              >
                Confirm
              </button>

              <button
                onClick={() => setShowPopup(false)}
                className="px-5 py-2 bg-gray-400 text-white rounded-lg"
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </AppLayout>
  );
}
