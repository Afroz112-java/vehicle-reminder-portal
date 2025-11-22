// // // // import React from "react";
// // // import { Bell, Car, CircleAlert, CirclePlus, LayoutDashboard, Settings, ShieldAlert } from "lucide-react";
// // // import { Link, useLocation } from "react-router-dom";

// // // function Icon({ name, className = "h-5 w-5" }) {
// // //   switch (name) {
// // //     case "car":
// // //       return (
// // //         <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor">
// // //           <path
// // //             d="M3 13.5V8.5C3 7.6716 3.6716 7 4.5 7h15c.8284 0 1.5.6716 1.5 1.5v5"
// // //             strokeWidth="1.5"
// // //             strokeLinecap="round"
// // //             strokeLinejoin="round"
// // //           />
// // //           <path
// // //             d="M5 16.5a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3zM19 16.5a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3z"
// // //             strokeWidth="1.5"
// // //             strokeLinecap="round"
// // //             strokeLinejoin="round"
// // //           />
// // //         </svg>
// // //       );
// // //     default:
// // //       return (
// // //         <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor">
// // //           <circle cx="12" cy="12" r="10" strokeWidth="1.5" />
// // //         </svg>
// // //       );
// // //   }
// // // }

// // // export default function Sidebar() {
// // //   const { pathname } = useLocation();

// // //   return (
// // //     <aside className="w-64 bg-white border-r min-h-screen hidden md:block">
// // //       <div className="p-4 border-b flex items-center gap-3">
// // //         <div className="h-10 w-10 bg-blue-500 text-white rounded-lg flex items-center justify-center">
// // //           {/* <Icon name="car" className="h-5 w-5 text-white" /> */}
// // //            <Car />
// // //         </div>
// // //         <div>
// // //           <div className="font-semibold">Konic Vehicle</div>
// // //           <div className="text-xs text-slate-400">Insurance Reminder</div>
// // //         </div>
// // //       </div>

// // //       <nav className="p-4">
// // //         <div className="text-xs text-slate-400 mb-2">Navigation</div>

// // //         <ul className="space-y-1">

// // //           {/* Dashboard */}
// // //           <li>
// // //             <Link
// // //               to="/dashboard"
// // //               className={`rounded-md px-3 py-2 flex items-center gap-3 
// // //                 ${pathname === "/dashboard" ? "bg-blue-50 text-blue-700" : "hover:bg-slate-100 text-slate-700"}
// // //               `}
// // //             >
// // //               {/* <Icon name="car" /> */}
// // //               <LayoutDashboard />
// // //               <span>Dashboard</span>
// // //             </Link>
// // //           </li>

// // //           {/* Vehicles */}
// // //           <li>
// // //             <Link
// // //               to="/vehicles"
// // //               className={`rounded-md px-3 py-2 flex items-center gap-3 
// // //                 ${pathname === "/vehicles" ? "bg-blue-50 text-blue-700" : "hover:bg-slate-100 text-slate-700"}
// // //               `}
// // //             >
// // //               {/* <Icon name="car" /> */}
// // //                  <Car />
// // //               <span>Vehicles</span>
// // //             </Link>
// // //           </li>

// // //           {/* Insurance Status */}
// // //           <li>
// // //             <Link
// // //               to="/insurance-status"
// // //               className={`rounded-md px-3 py-2 flex items-center gap-3 
// // //                 ${pathname === "/insurance-status" ? "bg-blue-50 text-blue-700" : "hover:bg-slate-100 text-slate-700"}
// // //               `}
// // //             >
// // //               <svg className="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor">
// // //                 <CircleAlert />
                
// // //               </svg>
// // //               <span>Insurance Status</span>
// // //             </Link>
// // //           </li>

// // //           {/* Add Vehicle */}
// // //           <li>
// // //             <Link
// // //               to="/add-vehicle"
// // //               className={`rounded-md px-3 py-2 flex items-center gap-3 
// // //                 ${pathname === "/add-vehicle" ? "bg-blue-50 text-blue-700" : "hover:bg-slate-100 text-slate-700"}
// // //               `}
// // //             >
// // //               <svg className="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor">
// // //                 {/* <path d="M12 5v14M5 12h14" strokeWidth="1.5" /> */}
// // //                     <CirclePlus />

// // //               </svg>
// // //               <span>Add Vehicle</span>
// // //             </Link>
// // //           </li>

// // //           {/* Reminders */}
// // //           <li>
// // //             <Link
// // //               to="/reminders"
// // //               className={`rounded-md px-3 py-2 flex items-center gap-3 
// // //                 ${pathname === "/reminders" ? "bg-blue-50 text-blue-700" : "hover:bg-slate-100 text-slate-700"}
// // //               `}
// // //             >
// // //               <svg className="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor">
// // //                 {/* <path d="M12 20v-6" strokeWidth="1.5" /> */}
// // //                  <Bell />
// // //               </svg>
// // //               <span>Reminders</span>
// // //             </Link>
// // //           </li>

// // //           {/* Settings */}
// // //           <li>
// // //             <Link
// // //               to="/settings"
// // //               className={`rounded-md px-3 py-2 flex items-center gap-3 
// // //                 ${pathname === "/settings" ? "bg-blue-50 text-blue-700" : "hover:bg-slate-100 text-slate-700"}
// // //               `}
// // //             >
// // //               <svg className="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor">
// // //                 {/* <path d="M12 6v6" strokeWidth="1.5" /> */}
// // //                 <Settings />
// // //               </svg>
// // //               <span>Settings</span>
// // //             </Link>
// // //           </li>

// // //         </ul>
// // //       </nav>
// // //     </aside>
// // //   );
// // // }
// import React, { useState } from "react";
// import { LayoutDashboard, Car, CircleAlert, CirclePlus, Bell, Settings, Users, LogOut } from "lucide-react";
// import { Link, useLocation } from "react-router-dom";

// export default function Sidebar() {
//   const [isCollapsed, setIsCollapsed] = useState(false);
//   const { pathname } = useLocation();

//   const links = [
//     { name: "Dashboard", icon: <LayoutDashboard />, path: "/dashboard" },
//     { name: "Vehicles", icon: <Car />, path: "/vehicles" },
//     { name: "Insurance Status", icon: <CircleAlert />, path: "/insurance-status" },
//     { name: "Add Vehicle", icon: <CirclePlus />, path: "/add-vehicle" },
//     { name: "Reminders", icon: <Bell />, path: "/reminders" },
//     { name: "Settings", icon: <Settings />, path: "/settings" },
//   ];

//   return (
//     <aside
//       className={`bg-white border-r min-h-screen flex flex-col justify-between transition-all duration-300 ${
//         isCollapsed ? "w-16" : "w-64"
//       }`}
//     >
//       {/* Top Section */}
//       <div>
//         {/* Logo */}
//         <div className="p-4 border-b flex items-center gap-3">
//           <div className="h-10 w-10 bg-blue-500 text-white rounded-lg flex items-center justify-center">
//             <Car />
//           </div>
//           {!isCollapsed && (
//             <div>
//               <div className="font-semibold">Konic Vehicle</div>
//               <div className="text-xs text-slate-400">Insurance Reminder</div>
//             </div>
//           )}
//         </div>

//         {/* Collapse Button */}
//         <button
//           className="p-4 w-full text-left hover:bg-gray-100 focus:outline-none"
//           onClick={() => setIsCollapsed(!isCollapsed)}
//         >
//           {isCollapsed ? "➡️" : "⬅️"} {!isCollapsed && "Collapse"}
//         </button>

//         {/* Navigation Links */}
//         <nav className="p-4">
//           <ul className="space-y-1">
//             {links.map((link) => (
//               <li key={link.path}>
//                 <Link
//                   to={link.path}
//                   className={`flex items-center gap-3 rounded-md px-3 py-2 ${
//                     pathname === link.path
//                       ? "bg-blue-50 text-blue-700"
//                       : "text-slate-700 hover:bg-slate-100"
//                   }`}
//                 >
//                   {link.icon}
//                   {!isCollapsed && <span>{link.name}</span>}
//                 </Link>
//               </li>
//             ))}
//           </ul>
//         </nav>
//       </div>

//       {/* Bottom Section */}
//       <div className="p-4 border-t border-gray-200">
//         <div className="flex items-center gap-3 cursor-pointer hover:bg-slate-100 p-2 rounded">
//           <Users />
//           {!isCollapsed && <span>Profile</span>}
//         </div>
//         <div className="flex items-center gap-3 cursor-pointer hover:bg-slate-100 p-2 rounded mt-2">
//           <LogOut />
//           {!isCollapsed && <span>Logout</span>}
//         </div>
//       </div>
//     </aside>
//   );
// }
import React, { useState } from "react";
import { LayoutDashboard, Car, CircleAlert, CirclePlus, Bell, Settings, Users, LogOut } from "lucide-react";
import { Link, useLocation, useNavigate } from "react-router-dom";

export default function Sidebar() {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const { pathname } = useLocation();
  const navigate = useNavigate();


  const links = [
    { name: "Dashboard", icon: <LayoutDashboard />, path: "/dashboard" },
    { name: "Vehicles", icon: <Car />, path: "/vehicles" },
    { name: "Insurance Status", icon: <CircleAlert />, path: "/insurance-status" },
    { name: "Add Vehicle", icon: <CirclePlus />, path: "/add-vehicle" },
    { name: "Reminders", icon: <Bell />, path: "/reminders" },
    { name: "Settings", icon: <Settings />, path: "/settings" },
  ];

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/");
  }

  return (
    <aside
      className={`bg-white border-r min-h-screen flex flex-col justify-between transition-all duration-300 ${
        isCollapsed ? "w-16" : "w-64"
      }`}
    >
      {/* Top Section */}
      <div>
        {/* Logo */}
        <div className="p-4 border-b flex items-center gap-3">
          <div className="h-10 w-10 bg-blue-500 text-white rounded-lg flex items-center justify-center">
            <Car />
          </div>
          {!isCollapsed && (
            <div>
              <div className="font-semibold">Konic Vehicle</div>
              <div className="text-xs text-slate-400">Insurance Reminder</div>
            </div>
          )}
        </div>

        {/* Collapse Button */}
        <button
          className="p-4 w-full text-left hover:bg-gray-100 focus:outline-none"
          onClick={() => setIsCollapsed(!isCollapsed)}
        >
          {isCollapsed ? "➡️" : "⬅️"} {!isCollapsed && "Collapse"}
        </button>

        {/* Navigation Links */}
        <nav className="p-4">
          <ul className="space-y-1">
            {links.map((link) => (
              <li key={link.path}>
                <Link
                  to={link.path}
                  className={`flex items-center gap-3 rounded-md px-3 py-2 ${
                    pathname === link.path
                      ? "bg-blue-50 text-blue-700"
                      : "text-slate-700 hover:bg-slate-100"
                  }`}
                >
                  {link.icon}
                  {!isCollapsed && <span>{link.name}</span>}
                </Link>
              </li>
            ))}
          </ul>
        </nav>
      </div>

      {/* Bottom Section */}
      <div className="p-4 border-t border-gray-200">
  <Link
    to="/profile"
    className="flex items-center gap-3 cursor-pointer hover:bg-slate-100 p-2 rounded"
  >
    <Users />
    {!isCollapsed && <span>Profile</span>}
  </Link>

  {/* Logout redirect to home */}
  <button
  onClick={handleLogout}
    className="flex  w-full items-center gap-3 cursor-pointer hover:bg-slate-100 p-2 rounded mt-2"
  >
    <LogOut />
    {!isCollapsed && <span>Logout</span>}
  </button>
</div>
    </aside>
  );
}
