import React, { useState, useEffect } from 'react';
// import { CAR1,CAR2, CAR3}from '../assets'
import CAR3 from '../assets/CAR3.jpg';
import CAR2 from '../assets/CAR2.jpg';
import CAR4 from '../assets/CAR4.jpg';
import About1 from '../assets/About1.png';



function CarouselComponent() {
    const [currentSlide, setCurrentSlide] = useState(0);

    const slides = [
        { src:About1, alt: "First slide" },
        { src:CAR2, alt: "Second slide" },
        { src:CAR3, alt: "Third slide" }
    ];

    useEffect(() => {
        const interval = setInterval(() => {
            setCurrentSlide((prev) => (prev + 1) % slides.length);
        }, 2000);
        return () => clearInterval(interval);
    }, [slides.length]);

    const goToSlide = (index) => {
        setCurrentSlide(index);
    };

    return (
        <div className="w-full max-w-6xl mx-auto">
            {/* Carousel Container */}
            <div className="relative w-full overflow-hidden rounded-lg shadow-lg">
                {/* Images */}
                <div className="relative w-full h-96">
                    {slides.map((slide, index) => (
                        <div
                            key={index}
                            className={`absolute inset-0 transition-opacity duration-3000 ease-in-out ${
                                index === currentSlide ? 'opacity-100' : 'opacity-0'
                            }`}
                        >
                            <img
                                src={slide.src}
                                alt={slide.alt}
                                className="w-full h-full object-cover"
                            />
                        </div>
                    ))}
                </div>

                {/* Previous Button */}
                <button
                    onClick={() => setCurrentSlide((prev) => (prev - 1 + slides.length) % slides.length)}
                    className="absolute left-4 top-1/2 transform -translate-y-1/2 bg-black/50 hover:bg-black/75 text-white p-2 rounded-full transition-colors z-10"
                >
                    &#10094;
                </button>

                {/* Next Button */}
                <button
                    onClick={() => setCurrentSlide((prev) => (prev + 1) % slides.length)}
                    className="absolute right-4 top-1/2 transform -translate-y-1/2 bg-black/50 hover:bg-black/75 text-white p-2 rounded-full transition-colors z-10"
                >
                    &#10095;
                </button>
            </div>

            {/* Dots Indicator */}
            <div className="flex justify-center gap-2 mt-4">
                {slides.map((_, index) => (
                    <button
                        key={index}
                        onClick={() => goToSlide(index)}
                        className={`w-4 h-4 rounded-full transition-colors ${
                            index === currentSlide ? 'bg-blue-600' : 'bg-gray-300 hover:bg-gray-400'
                        }`}
                        aria-label={`Go to slide ${index + 1}`}
                    />
                ))}
            </div>
        </div>
    );
}

export default CarouselComponent;