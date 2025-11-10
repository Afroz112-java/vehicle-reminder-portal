package com.example.vehicle.service;

import com.example.vehicle.entity.Vehicle;
import com.example.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VehicleService {

   @Autowired
   private VehicleRepository vehicleRepository;

   // 1. Method to add a new vehicle
    public Vehicle addVehicle(Vehicle v) {
        return vehicleRepository.save(v);
    }

    // 2. Method to retrieve all vehicles
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    // 3. Method to retrieve a vehicle by ID
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    // 4. Method to update a vehicle
    public Vehicle updateVehicle(Long id, Vehicle vehicleDetails) {
        return vehicleRepository.findById(id)
                .map(existingVehicle -> {
                    existingVehicle.setRegNumber(vehicleDetails.getRegNumber());
                    existingVehicle.setBrand(vehicleDetails.getBrand());
                    existingVehicle.setModel(vehicleDetails.getModel());
                    existingVehicle.setInsuranceExpiryDate(vehicleDetails.getInsuranceExpiryDate());
                    existingVehicle.setServiceDueDate(vehicleDetails.getServiceDueDate());
                    existingVehicle.setUpdatedAt(LocalDateTime.now());
                    return vehicleRepository.save(existingVehicle);
                })
                .orElse(null);
    }
    // 5. Method to delete a vehicle
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
