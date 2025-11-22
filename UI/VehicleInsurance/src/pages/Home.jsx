import React from "react";
import CarouselComponent from "./CarouselComponent";
import Footer from "../components/Footer";
import Navbar from "../components/Navbar";

const Home = () => {
  return (
    <div className="min-h-screen bg-linear-to-b from-white to-gray-100">
      {/* Navbar */}
      <Navbar />
 

      {/* Hero Section */}
      <section className="flex flex-col md:flex-row items-center justify-between px-10 md:px-20 py-16">
        {/* Left Content */}
        <div className="md:w-1/2 space-y-6">
          <h1 className="text-6xl font-extrabold leading-tight text-gray-900">
            Welcome!
          </h1>
          <h2 className="text-5xl font-extrabold leading-snug">
            Start your vehicle journey with{" "}
            <span className="text-sky-500">
              Konic Vehicle Insurance Reminder
            </span>
          </h2>
          <p className="text-gray-500 text-lg">
            Smart Solutions for Vehicle Management
          </p>
        </div>

        {/* Right Image / Carousel */}
        <div className="md:w-1/2 mt-10 md:mt-0 flex justify-center">
          <CarouselComponent />
        </div>
      </section>

      {/* Footer */}
      <Footer />
      
    </div>
  );
};

export default Home;
