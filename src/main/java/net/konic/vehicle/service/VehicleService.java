package net.konic.vehicle.service;

import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.repository.VehicleRepository;
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
    public Vehicle updateVehicle(Long id, Vehicle v) {
       v.setId(id);
       v.setUpdatedAt(LocalDateTime.now());
         return vehicleRepository.save(v);
    }
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
