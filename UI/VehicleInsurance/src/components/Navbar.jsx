import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Navbar = () => {
  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Check token on mount
  useEffect(() => {
    setIsAuthenticated(!!localStorage.getItem("token"));
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    setIsAuthenticated(false); // update state
    navigate("/");
  };

  const handleDashboard = () => {
    navigate("/dashboard");
  };

  return (
    <nav className="flex justify-between items-center px-10 py-4 shadow-sm bg-sky-700">
      {/* Left - Logo + Name */}
      <div
        className="flex items-center space-x-3 cursor-pointer"
        onClick={() => navigate("/")}
      >
        <div className="bg-gradient-to-r from-sky-500 to-teal-400 text-white font-bold text-lg rounded-full w-10 h-10 flex items-center justify-center">
          K
        </div>
        <h1 className="text-2xl font-semibold text-white">
          Konic Vehicle Insurance Reminder
        </h1>
      </div>

      {/* Right - Buttons */}
      <div className="flex items-center space-x-4">
        <button
          onClick={() => navigate("/about")}
          className="bg-gradient-to-r from-sky-500 to-teal-400 text-white font-semibold px-5 py-2 rounded-xl shadow-md hover:opacity-90 transition"
        >
          About
        </button>

        {isAuthenticated ? (
          <>
            <button
              onClick={handleDashboard}
              className="bg-gray-100 text-gray-700 font-medium px-5 py-2 rounded-xl shadow-sm hover:bg-gray-200 transition"
            >
              Dashboard
            </button>
            <button
              onClick={handleLogout}
              className="bg-gradient-to-r from-sky-500 to-teal-400 text-white font-semibold px-5 py-2 rounded-xl shadow-md hover:opacity-90 transition"
            >
              Logout
            </button>
          </>
        ) : (
          <>
            <button
              onClick={() => navigate("/register")}
              className="bg-gradient-to-r from-sky-500 to-teal-400 text-white font-semibold px-5 py-2 rounded-xl shadow-md hover:opacity-90 transition"
            >
              Register
            </button>
            <button
              onClick={() => navigate("/login")}
              className="bg-gray-100 text-gray-700 font-medium px-5 py-2 rounded-xl shadow-sm hover:bg-gray-200 transition"
            >
              Login
            </button>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
