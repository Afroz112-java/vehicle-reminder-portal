import React, { useState } from "react";
import AppLayout from "../components/AppLayout";
import Sidebar from "../components/Sidebar";
import {
  User,
  Bell,
  Shield,
  Moon,
  Sun,
  Trash2,
  LogOut,
  Mail,
  Phone,
} from "lucide-react";

export default function Settings() {
  const [darkMode, setDarkMode] = useState(false);
  const [emailNotif, setEmailNotif] = useState(true);
  const [smsNotif, setSmsNotif] = useState(false);

  return (
    <AppLayout>
      <div className="p-10">
        {/* Header */}
        <h1 className="text-4xl font-bold text-gray-900">Settings</h1>
        <p className="text-gray-600 mt-1">Manage your account preferences</p>

        {/* Main Card */}
        <div className="mt-8 bg-white shadow rounded-xl p-6 space-y-10">

          {/* Profile Section */}
          <div>
            <h2 className="text-xl font-semibold flex items-center gap-2">
              <User size={20} /> Profile Information
            </h2>

            <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="text-gray-600">Full Name</label>
                <input
                  type="text"
                  className="w-full mt-2 px-4 py-2 border rounded-lg bg-gray-50"
                  value="Shaik Mohammad Asif"
                  readOnly
                />
              </div>

              <div>
                <label className="text-gray-600">Email Address</label>
                <div className="flex items-center mt-2 px-4 py-2 border rounded-lg bg-gray-50">
                  <Mail size={18} className="text-gray-500 mr-2" />
                  <input
                    type="email"
                    value="asif@example.com"
                    className="w-full bg-transparent outline-none"
                    readOnly
                  />
                </div>
              </div>

              <div>
                <label className="text-gray-600">Phone Number</label>
                <div className="flex items-center mt-2 px-4 py-2 border rounded-lg bg-gray-50">
                  <Phone size={18} className="text-gray-500 mr-2" />
                  <input
                    type="text"
                    value="+91 98765 43210"
                    className="w-full bg-transparent outline-none"
                    readOnly
                  />
                </div>
              </div>
            </div>
          </div>

          {/* Notification Section */}
          <div>
            <h2 className="text-xl font-semibold flex items-center gap-2">
              <Bell size={20} /> Notification Preferences
            </h2>

            <div className="mt-4 space-y-4">

              <div className="flex items-center justify-between border p-3 rounded-lg">
                <div className="flex items-center gap-3">
                  <Mail size={18} className="text-blue-600" />
                  <span>Email Notifications</span>
                </div>

                <label className="relative inline-flex items-center cursor-pointer">
                  <input
                    type="checkbox"
                    checked={emailNotif}
                    onChange={() => setEmailNotif(!emailNotif)}
                    className="sr-only peer"
                  />
                  <div className="w-11 h-6 bg-gray-300 peer-focus:ring-2 
                               peer-focus:ring-blue-300 rounded-full peer
                               peer-checked:bg-blue-600"></div>
                </label>
              </div>

              <div className="flex items-center justify-between border p-3 rounded-lg">
                <div className="flex items-center gap-3">
                  <Phone size={18} className="text-green-600" />
                  <span>SMS Reminders</span>
                </div>

                <label className="relative inline-flex items-center cursor-pointer">
                  <input
                    type="checkbox"
                    checked={smsNotif}
                    onChange={() => setSmsNotif(!smsNotif)}
                    className="sr-only peer"
                  />
                  <div className="w-11 h-6 bg-gray-300 peer-focus:ring-2 
                               peer-focus:ring-blue-300 rounded-full peer
                               peer-checked:bg-blue-600"></div>
                </label>
              </div>

            </div>
          </div>

          {/* Theme Section */}
          <div>
            <h2 className="text-xl font-semibold flex items-center gap-2">
              <Shield size={20} /> Display & Theme
            </h2>

            <div className="mt-4 flex items-center justify-between border p-3 rounded-lg">
              <div className="flex items-center gap-3">
                {darkMode ? (
                  <Moon size={20} className="text-purple-600" />
                ) : (
                  <Sun size={20} className="text-yellow-500" />
                )}
                <span>Dark Mode</span>
              </div>

              <label className="relative inline-flex items-center cursor-pointer">
                <input
                  type="checkbox"
                  checked={darkMode}
                  onChange={() => setDarkMode(!darkMode)}
                  className="sr-only peer"
                />
                <div className="w-11 h-6 bg-gray-300 peer-focus:ring-2 
                             peer-focus:ring-blue-300 rounded-full peer
                             peer-checked:bg-blue-600"></div>
              </label>
            </div>
          </div>

          {/* Account Actions */}
          <div>
            <h2 className="text-xl font-semibold flex items-center gap-2">
              <User size={20} /> Account Management
            </h2>

            <div className="mt-4 space-y-4">

              <button className="w-full flex items-center justify-between p-3 border rounded-lg hover:bg-gray-100">
                <span className="flex items-center gap-3">
                  <Trash2 size={18} className="text-red-600" />
                  Delete Account
                </span>
              </button>

              <button className="w-full flex items-center justify-between p-3 border rounded-lg hover:bg-gray-100">
                <span className="flex items-center gap-3">
                  <LogOut size={18} className="text-blue-600" />
                  Logout
                </span>
              </button>

            </div>
          </div>

        </div>
      </div>
    </AppLayout>
  );
}
