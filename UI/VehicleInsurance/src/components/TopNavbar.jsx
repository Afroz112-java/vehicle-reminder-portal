import React from "react";

export default function TopNavbar() {
  return (
    <header className="border-b bg-white p-4 flex items-center justify-between">
      <div className="flex items-center gap-4">
        <button className="md:hidden p-2 rounded-md bg-slate-100">☰</button>
        <div>
          <div className="text-sm text-slate-500">Welcome, <span className="font-semibold text-slate-900">Asif</span></div>
          <div className="text-xs text-slate-400">Have a great day!</div>
        </div>
      </div>

      <div className="flex items-center gap-4">
        <button className="relative">
          <svg className="h-6 w-6 text-slate-700" viewBox="0 0 24 24" fill="none" stroke="currentColor"><path d="M15 17h5l-1.405-1.405A2.032 2.032 0 0 1 18 14.158V11a6 6 0 1 0-12 0v3.159c0 .538-.214 1.055-.595 1.436L4 17h5" strokeWidth="1.5"/></svg>
          <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full h-3 w-3 flex items-center justify-center"></span>
        </button>
        <div className="h-10 w-10 rounded-full bg-blue-500 text-white flex items-center justify-center font-semibold">AS</div>
      </div>
    </header>
  );
}
