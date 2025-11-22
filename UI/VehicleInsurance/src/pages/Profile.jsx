import React, { useState } from "react";
import { Camera, Mail, Phone, User, Lock } from "lucide-react";
import AppLayout from "../components/AppLayout";
import Sidebar from "../components/Sidebar";
const Profile = () => {
  const [profile, setProfile] = useState({
    name: "Shaik Mohammad Asif",
    email: "asif@gmail.com",
    phone: "9876543210",
  });

  const [passwords, setPasswords] = useState({
    current: "",
    newPass: "",
    confirmPass: "",
  });

  const handleProfileChange = (e) => {
    setProfile({ ...profile, [e.target.name]: e.target.value });
  };

  const handlePasswordChange = (e) => {
    setPasswords({ ...passwords, [e.target.name]: e.target.value });
  };

  return (
    <AppLayout>
      <div className="p-10">
        <h1 className="text-4xl font-bold text-gray-900 mb-6">Profile</h1>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">

          {/* Profile Card */}
          <div className="bg-white p-6 shadow rounded-xl flex flex-col items-center">
            <div className="relative">
              <img
                src="https://via.placeholder.com/120"
                alt="Profile"
                className="w-32 h-32 rounded-full object-cover"
              />
              <button className="absolute bottom-1 right-1 bg-blue-600 p-2 rounded-full text-white shadow hover:bg-blue-700">
                <Camera size={16} />
              </button>
            </div>

            <h2 className="text-xl font-semibold mt-4">{profile.name}</h2>
            <p className="text-gray-500">{profile.email}</p>
          </div>

          {/* Profile Info Form */}
          <div className="bg-white p-6 shadow rounded-xl md:col-span-2">
            <h2 className="text-xl font-semibold mb-4">Personal Information</h2>

            <div className="space-y-5">
              {/* Name */}
              <div>
                <label className="text-sm font-medium text-gray-600 flex items-center gap-2">
                  <User size={16} /> Full Name
                </label>
                <input
                  type="text"
                  name="name"
                  value={profile.name}
                  onChange={handleProfileChange}
                  className="w-full mt-1 px-4 py-2 border rounded-lg bg-gray-50 focus:ring focus:ring-blue-200 outline-none"
                />
              </div>

              {/* Email */}
              <div>
                <label className="text-sm font-medium text-gray-600 flex items-center gap-2">
                  <Mail size={16} /> Email Address
                </label>
                <input
                  type="email"
                  name="email"
                  value={profile.email}
                  onChange={handleProfileChange}
                  className="w-full mt-1 px-4 py-2 border rounded-lg bg-gray-50 focus:ring focus:ring-blue-200 outline-none"
                />
              </div>

              {/* Phone */}
              <div>
                <label className="text-sm font-medium text-gray-600 flex items-center gap-2">
                  <Phone size={16} /> Phone Number
                </label>
                <input
                  type="text"
                  name="phone"
                  value={profile.phone}
                  onChange={handleProfileChange}
                  className="w-full mt-1 px-4 py-2 border rounded-lg bg-gray-50 focus:ring focus:ring-blue-200 outline-none"
                />
              </div>

              <button className="mt-4 px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
                Save Changes
              </button>
            </div>
          </div>
        </div>

        {/* Password Section */}
        <div className="mt-10 bg-white p-6 shadow rounded-xl md:w-1/2">
          <h2 className="text-xl font-semibold mb-4 flex items-center gap-2">
            <Lock size={20} /> Change Password
          </h2>

          <div className="space-y-5">
            <div>
              <label className="text-sm font-medium text-gray-600">
                Current Password
              </label>
              <input
                type="password"
                name="current"
                value={passwords.current}
                onChange={handlePasswordChange}
                className="w-full mt-1 px-4 py-2 border rounded-lg bg-gray-50"
              />
            </div>

            <div>
              <label className="text-sm font-medium text-gray-600">
                New Password
              </label>
              <input
                type="password"
                name="newPass"
                value={passwords.newPass}
                onChange={handlePasswordChange}
                className="w-full mt-1 px-4 py-2 border rounded-lg bg-gray-50"
              />
            </div>

            <div>
              <label className="text-sm font-medium text-gray-600">
                Confirm Password
              </label>
              <input
                type="password"
                name="confirmPass"
                value={passwords.confirmPass}
                onChange={handlePasswordChange}
                className="w-full mt-1 px-4 py-2 border rounded-lg bg-gray-50"
              />
            </div>

            <button className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
              Update Password
            </button>
          </div>
        </div>
      </div>
    </AppLayout>
  );
};

export default Profile;
