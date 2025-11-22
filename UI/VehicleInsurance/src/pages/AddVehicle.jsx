
import React, { useState } from "react";
import axios from "axios";
import AppLayout from "../components/AppLayout";
import { useNavigate } from "react-router-dom";

export default function AddVehicle() {
  const navigate = useNavigate();

  const [vehicle, setVehicle] = useState({
    regNumber: "",
    brand: "",
    model: "",
    insuranceExpiryDate: "",
    serviceDueDate: "",
    ownerName: "",
    email: "",
    active: true,
    vehicleType: "CAR",
    user: {
      name: "",
      email: "",
      phone: "",
      role: "USER",
    }
  });

  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setVehicle({ ...vehicle, [e.target.name]: e.target.value });
  };

  const handleUserChange = (e) => {
    setVehicle({
      ...vehicle,
      user: { ...vehicle.user, [e.target.name]: e.target.value },
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const token = localStorage.getItem("token");

      await axios.post("http://localhost:8081/api/vehicle", vehicle, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      alert("Vehicle added successfully!");
      navigate("/vehicles");
    } catch (error) {
      console.error("Error adding vehicle:", error);
      alert("Failed to add vehicle. Check backend or token.");
    }

    setLoading(false);
  };

  return (
    <AppLayout>
      <div className="min-h-screen flex items-center justify-center bg-gray-100 p-6">
        <div className="w-full max-w-md bg-white p-6 rounded-2xl shadow-lg border">
          <h2 className="text-2xl font-semibold text-center mb-4">
            Add New Vehicle
          </h2>

          <form onSubmit={handleSubmit} className="space-y-4">

            {/* Registration Number */}
            <div>
              <label className="block text-gray-700 mb-1">Registration Number *</label>
              <input
                type="text"
                name="regNumber"
                value={vehicle.regNumber}
                onChange={handleChange}
                className="w-full p-2 border rounded-md"
                placeholder="TS07AP1111"
                required
              />
            </div>

            {/* Brand */}
            <div>
              <label className="block text-gray-700 mb-1">Brand *</label>
              <input
                type="text"
                name="brand"
                value={vehicle.brand}
                onChange={handleChange}
                className="w-full p-2 border rounded-md"
                placeholder="Suzuki, Hyundai..."
                required
              />
            </div>

            {/* Model */}
            <div>
              <label className="block text-gray-700 mb-1">Model *</label>
              <input
                type="text"
                name="model"
                value={vehicle.model}
                onChange={handleChange}
                className="w-full p-2 border rounded-md"
                placeholder="Solio, i20, Activa..."
                required
              />
            </div>

            {/* Owner Name */}
            <div>
              <label className="block text-gray-700 mb-1">Owner Name *</label>
              <input
                type="text"
                name="ownerName"
                value={vehicle.ownerName}
                onChange={handleChange}
                className="w-full p-2 border rounded-md"
                placeholder="Pavan"
                required
              />
            </div>

            {/* Owner Email */}
            <div>
              <label className="block text-gray-700 mb-1">Owner Email *</label>
              <input
                type="email"
                name="email"
                value={vehicle.email}
                onChange={handleChange}
                className="w-full p-2 border rounded-md"
                placeholder="example@gmail.com"
                required
              />
            </div>

            {/* Insurance Expiry Date */}
            <div>
              <label className="block text-gray-700 mb-1">Insurance Expiry Date *</label>
              <input
                type="date"
                name="insuranceExpiryDate"
                value={vehicle.insuranceExpiryDate}
                onChange={handleChange}
                className="w-full p-2 border rounded-md"
                required
              />
            </div>

            {/* Service Due Date */}
            <div>
              <label className="block text-gray-700 mb-1">Service Due Date *</label>
              <input
                type="date"
                name="serviceDueDate"
                value={vehicle.serviceDueDate}
                onChange={handleChange}
                className="w-full p-2 border rounded-md"
                required
              />
            </div>

            {/* Vehicle Type */}
            <div>
              <label className="block text-gray-700 mb-1">Vehicle Type *</label>
              <select
                name="vehicleType"
                value={vehicle.vehicleType}
                onChange={handleChange}
                className="w-full p-2 border rounded-md"
              >
                <option value="CAR">Car</option>
                <option value="BIKE">Bike</option>
                <option value="TRUCK">Truck</option>
              </select>
            </div>

            {/* USER BLOCK */}
            <h3 className="text-lg font-semibold mt-4">User Details</h3>

            {/* User Name */}
            <div>
              <label className="block text-gray-700 mb-1">User Name *</label>
              <input
                type="text"
                name="name"
                value={vehicle.user.name}
                onChange={handleUserChange}
                className="w-full p-2 border rounded-md"
                required
              />
            </div>

            {/* User Email */}
            <div>
              <label className="block text-gray-700 mb-1">User Email *</label>
              <input
                type="email"
                name="email"
                value={vehicle.user.email}
                onChange={handleUserChange}
                className="w-full p-2 border rounded-md"
                required
              />
            </div>

            {/* User Phone */}
            <div>
              <label className="block text-gray-700 mb-1">Phone *</label>
              <input
                type="text"
                name="phone"
                value={vehicle.user.phone}
                onChange={handleUserChange}
                className="w-full p-2 border rounded-md"
                required
              />
            </div>

            {/* User Role */}
            <div>
              <label className="block text-gray-700 mb-1">Role</label>
              <input
                type="text"
                name="role"
                value={vehicle.user.role}
                onChange={handleUserChange}
                className="w-full p-2 border rounded-md"
                readOnly
              />
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              className="w-full bg-blue-600 hover:bg-blue-700 text-white py-2 rounded-md mt-2"
              disabled={loading}
            >
              {loading ? "Adding..." : "Add Vehicle"}
            </button>

          </form>
        </div>
      </div>
    </AppLayout>
  );
}

