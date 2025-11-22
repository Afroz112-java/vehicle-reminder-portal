import React from "react";

function Icon({ name, className = "h-6 w-6" }) {
  switch (name) {
    case "car":
      return <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor"><path d="M3 13.5V8.5C3 7.6716 3.6716 7 4.5 7h15c.8284 0 1.5.6716 1.5 1.5v5" strokeWidth="1.5"/><path d="M5 16.5a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3zM19 16.5a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3z" strokeWidth="1.5"/></svg>;
    case "warning":
      return <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" strokeWidth="1.3"/></svg>;
    case "x":
      return <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor"><path d="M18 6L6 18M6 6l12 12" strokeWidth="1.5"/></svg>;
    case "paper":
      return <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor"><path d="M21 8V7a2 2 0 0 0-2-2h-7" strokeWidth="1.3"/><path d="M3 6v12a2 2 0 0 0 2 2h12" strokeWidth="1.3"/></svg>;
    default:
      return <svg className={className} viewBox="0 0 24 24" fill="none" stroke="currentColor"><circle cx="12" cy="12" r="9" strokeWidth="1.5"/></svg>;
  }
}

export default function StatCard({ title, value, icon, bg = "bg-white", iconColor = "text-slate-700" }) {
  return (
    <div className="rounded-lg border bg-white p-4 shadow-sm flex items-center gap-4">
      <div className={`rounded-lg p-3 ${bg}`}>
        <Icon name={icon} className={`h-6 w-6 ${iconColor}`} />
      </div>
      <div>
        <p className="text-sm text-slate-500">{title}</p>
        <p className="text-2xl font-bold">{value}</p>
      </div>
    </div>
  );
}
