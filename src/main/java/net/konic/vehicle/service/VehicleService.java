package net.konic.vehicle.service;

import net.konic.vehicle.dto.ApiResponse;
import net.konic.vehicle.entity.UserEntity;
import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.repository.UserRepository;
import net.konic.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

@Service
public class VehicleService {
    @Autowired
    private UserRepository userRepository;


    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // Create vehicle
    @CacheEvict(value = {"vehicles", "vehicle"}, allEntries = true)
    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    // Get all vehicles
    @Cacheable("vehicles")
    public List<Vehicle> getAllVehicles() {
        System.out.println("Fetching vehicles from DB...");
        return vehicleRepository.findAll();
    }

    // Get vehicle by ID
    @Cacheable(value = "vehicle", key = "#vehicleId")
    public Vehicle getVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found: " + vehicleId));
    }

    // Update vehicle
    @CacheEvict(value = {"vehicles", "vehicle"}, allEntries = true)
    public Vehicle updateVehicle(Long vehicleId, Vehicle updatedVehicle) {
        Vehicle existingVehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found: " + vehicleId));

        existingVehicle.setRegNumber(updatedVehicle.getRegNumber());
        existingVehicle.setBrand(updatedVehicle.getBrand());
        existingVehicle.setModel(updatedVehicle.getModel());
        existingVehicle.setInsuranceExpiryDate(updatedVehicle.getInsuranceExpiryDate());
        existingVehicle.setServiceDueDate(updatedVehicle.getServiceDueDate());
        existingVehicle.setOwnerName(updatedVehicle.getOwnerName());
        existingVehicle.setActive(updatedVehicle.isActive());

        return vehicleRepository.save(existingVehicle);
    }

    // Delete vehicle
    @CacheEvict(value = {"vehicles", "vehicle"}, allEntries = true)
    public ApiResponse deleteVehicle(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            return new ApiResponse(false, "Vehicle not found: " + vehicleId);
        }
        vehicleRepository.deleteById(vehicleId);
        return new ApiResponse(true, "Vehicle deleted successfully");
    }

    // Dashboard stats
    public long getTotalVehicles() {
        return vehicleRepository.count();
    }

    public void saveUserAndVehiclesFromCsv(MultipartFile file) {

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {

            String[] row;
            boolean header = true;

            while ((row = reader.readNext()) != null) {

                if (header) {
                    header = false;
                    continue;
                }

                String fullName = row[0];
                String email = row[1];

                // Check if user already exists
                UserEntity user = userRepository.findByEmail(email)
                        .orElseGet(() -> {
                            UserEntity newUser = new UserEntity();
                            //newUser.getName();     // ✅ Correct
                            newUser.setName(fullName);
                            newUser.setEmail(email);   // ✅ Correct
                            newUser.setRole("USER");   // optional default role
                            return userRepository.save(newUser);
                        });


                // Create vehicle
                Vehicle vehicle = new Vehicle();
                vehicle.setRegNumber(row[2]);
                vehicle.setBrand(row[3]);
                vehicle.setModel(row[4]);
                vehicle.setInsuranceExpiryDate(LocalDate.parse(row[5]));
                vehicle.setServiceDueDate(LocalDate.parse(row[6]));
                vehicle.setActive(Boolean.parseBoolean(row[7]));
                vehicle.setUser(user); // Assign FK

                vehicleRepository.save(vehicle);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV: " + e.getMessage());
        }
    }

}
