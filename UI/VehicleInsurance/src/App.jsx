import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

// Components
import Register from "./components/Register";
import Login from "./components/Login";
import About from "./components/About";
import Sidebar from "./components/Sidebar";
// import TopNavbar from "./components/TopNavbar";
// import Dashboardlayout from "./components/DashboardLayout";
// Pages
import Home from "./pages/Home";
import Dashboard from "./pages/Dashboard";
import Vehicles from "./pages/Vehicles";
import InsuranceStatus from "./pages/InsuranceStatus";
// // import AddVehicle from "./pages/AddVehicle";
import Reminders from "./pages/Reminders";
import Settings from "./pages/Settings";
import AddVehicle from "./pages/AddVehicle";
import Profile from "./pages/Profile";
import ProtectedRoute from "./components/ProtectedRoute";
import PublicRoutes from "./components/PublicRoutes";
import { Toaster } from "react-hot-toast"; // <-- Add this
import toast from "react-hot-toast";
const App = () => {
  return (
    <Router>
      <Toaster position="top-right" reverseOrder={false} />
      <div className="min-h-screen">
        <Routes>
          {/* Public Pages */}
          <Route path="/" element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/register" element={<Register />} />

          <Route
            path="/login"
            element={
              <PublicRoutes>
                <Login />
              </PublicRoutes>
            }
          />
          {/* <Route path="/sidebar" element={<Sidebar />} /> */}
          {/* <Route path="/topnavbar" element={<TopNavbar />} />
<Route path="/dashboardlayout" element={<Dashboardlayout />} /> */}
          {/* Dashboard Pages */}

          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/add-vehicle"
            element={
              <ProtectedRoute>
                <AddVehicle />
              </ProtectedRoute>
            }
          />
          <Route
            path="/reminders"
            element={
              <ProtectedRoute>
                <Reminders />
              </ProtectedRoute>
            }
          />
          <Route
            path="/Insurance-Status"
            element={
              <ProtectedRoute>
                <InsuranceStatus />
              </ProtectedRoute>
            }
          />

          {/* <Route path="/add-vehicle" element={<AddVehicle />} /> */}
          {/* <Route path="/reminders" element={<Reminders />} /> */}
          <Route
            path="/settings"
            element={
              <ProtectedRoute>
                <Settings />
              </ProtectedRoute>
            }
          />
          <Route
            path="/profile"
            element={
              <ProtectedRoute>
                <Profile />
              </ProtectedRoute>
            }
          />
          <Route path="/vehicles">
            <Route
              index
              element={
                <ProtectedRoute>
                  <Vehicles />
                </ProtectedRoute>
              }
            />
            <Route
              path="add-vehicle"
              element={
                <ProtectedRoute>
                  <AddVehicle />
                </ProtectedRoute>
              }
            />
            {/* <Route path="/vehicles">
  <Route
    path="edit/:id"
    element={
      <ProtectedRoute>
        <EditVehicle />
      </ProtectedRoute>
    }
  />
</Route> */}
          </Route>
        </Routes>
      </div>
    </Router>
  );
};

export default App;
