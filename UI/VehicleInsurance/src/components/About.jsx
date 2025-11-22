import React from "react";
import { ShieldCheck, Bell, CalendarCheck, Clock } from "lucide-react";
import CarouselComponent from "../pages/CarouselComponent";
import { useNavigate } from "react-router-dom";
import { ArrowLeft, Home } from "lucide-react";

const About = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-linear-to-b from-white to-sky-50 text-gray-800">
      {/* Hero Section */}
      {/* <section className="text-center py-20 bg-linear-to-r from-sky-600 to-teal-400 text-white">
        <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
          About Konic Vehicle Insurance Reminder
        </h1>
        <p className="text-lg md:text-xl max-w-2xl mx-auto opacity-90">
          Your trusted partner to manage, track, and remind you about your vehicle insurance renewals — so you never miss a due date again.
        </p>
        <button
          onClick={() => navigate("/")}
          className="mt-4 px-6 py-3 bg-white text-sky-700 font-semibold rounded-full shadow-lg hover:bg-gray-100 transition"
        >
          ← Back to Home
        </button>
      </section> */
      
      <section className="relative text-center py-20 bg-linear-to-r from-sky-600 to-teal-400 text-white">

  {/* Back to Home Button */}
  <button
    onClick={() => navigate("/")}
    className="absolute left-6 top-6 flex items-center gap-2 bg-white text-sky-700 font-semibold px-4 py-2 rounded-full shadow-lg hover:bg-gray-100 transition"
  >
    <ArrowLeft className="w-5 h-5" />
    <Home className="w-5 h-5" />
  </button>

  {/* Heading */}
  <h1 className="text-4xl md:text-5xl font-extrabold mb-4">
    About Konic Vehicle Insurance Reminder
  </h1>

  {/* Subtext */}
  <p className="text-lg md:text-xl max-w-2xl mx-auto opacity-90">
    Your trusted partner to manage, track, and remind you about your vehicle insurance renewals — so you never miss a due date again.
  </p>
</section>
}

      {/* Main Content */}
      <section className="max-w-6xl mx-auto py-16 px-6 grid md:grid-cols-2 gap-10 items-center">
        <div>
          <CarouselComponent />
        </div>

        <div>
          <h2 className="text-3xl font-bold mb-4 text-sky-700">
            Simplifying Your Insurance Management
          </h2>
          <p className="text-gray-600 leading-relaxed mb-6">
            Konic Vehicle Insurance Reminder helps you stay ahead of your vehicle insurance deadlines with smart reminders, easy management tools, and personalized insights — all in one place.
          </p>
          <ul className="space-y-4">
            <li className="flex items-start space-x-3">
              <ShieldCheck className="text-sky-500 w-6 h-6 mt-1" />
              <span>Secure and private reminder system to protect your data.</span>
            </li>
            <li className="flex items-start space-x-3">
              <Bell className="text-sky-500 w-6 h-6 mt-1" />
              <span>Timely alerts via email and dashboard notifications.</span>
            </li>
            <li className="flex items-start space-x-3">
              <CalendarCheck className="text-sky-500 w-6 h-6 mt-1" />
              <span>Track all your vehicle insurance renewal dates easily.</span>
            </li>
            <li className="flex items-start space-x-3">
              <Clock className="text-sky-500 w-6 h-6 mt-1" />
              <span>Set custom reminders to stay informed in advance.</span>
            </li>
          </ul>
        </div>
      </section>

      {/* Mission Section */}
      <section className="bg-white py-16">
        <div className="max-w-5xl mx-auto text-center px-6">
          <h2 className="text-3xl font-bold text-sky-700 mb-4">Our Mission</h2>
          <p className="text-gray-600 leading-relaxed text-lg">
            At <span className="font-semibold text-sky-600">Konic</span>, our mission is to bring convenience and peace of mind to every vehicle owner by providing a simple, smart, and secure insurance reminder system — ensuring you’re always protected, always on time.
          </p>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-sky-700 text-white text-center py-6 mt-10">
        <p className="text-sm opacity-80">
          © {new Date().getFullYear()} Konic Vehicle Insurance Reminder. All rights reserved.
        </p>
      </footer>
    </div>
  );
};

export default About;
