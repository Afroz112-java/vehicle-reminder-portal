package com.example.vehicle.service;

import com.example.vehicle.entity.Vehicle;
import com.example.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

   @Autowired
   private VehicleRepository vehicleRepository;

   // Method to add a new vehicle
    public Vehicle addVehicle(Vehicle v) {
        return vehicleRepository.save(v);
    }

    // Method to retrieve all vehicles
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }
}
