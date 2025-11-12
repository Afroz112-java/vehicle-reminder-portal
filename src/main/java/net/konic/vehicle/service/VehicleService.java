package net.konic.vehicle.service;

import com.opencsv.CSVReader;
import net.konic.vehicle.dto.ApiResponse;
import net.konic.vehicle.entity.User;
import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.execption.InvalidInputException;
import net.konic.vehicle.execption.ResourceNotFoundException;
import net.konic.vehicle.repository.UserRepository;
import net.konic.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
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
        if (vehicle.getUser() == null || vehicle.getUser().getEmail() == null) {
            throw new InvalidInputException("User email must be provided to create a vehicle.");
        }

        String email = vehicle.getUser().getEmail();
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = vehicle.getUser();
            return userRepository.save(newUser);
        });

        vehicle.setUser(user);
        return vehicleRepository.save(vehicle);
    }

    // Get all vehicles
    @Cacheable("vehicles")
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            throw new ResourceNotFoundException("No vehicles found in the database.");
        }
        return vehicles;
    }

    // Get vehicle by ID
    @Cacheable(value = "vehicle", key = "#vehicleId")
    public Vehicle getVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId));
    }

    // Update vehicle
    @CacheEvict(value = {"vehicles", "vehicle"}, allEntries = true)
    public Vehicle updateVehicle(Long vehicleId, Vehicle updatedVehicle) {
        Vehicle existingVehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot update — Vehicle not found with ID: " + vehicleId));

        existingVehicle.setRegNumber(updatedVehicle.getRegNumber());
        existingVehicle.setBrand(updatedVehicle.getBrand());
        existingVehicle.setModel(updatedVehicle.getModel());
        existingVehicle.setInsuranceExpiryDate(updatedVehicle.getInsuranceExpiryDate());
        existingVehicle.setServiceDueDate(updatedVehicle.getServiceDueDate());
        // existingVehicle.setOwnerName(updatedVehicle.getOwnerName());
        // existingVehicle.setActive(updatedVehicle.isActive());

        return vehicleRepository.save(existingVehicle);
    }

    // Delete vehicle
    @CacheEvict(value = {"vehicles", "vehicle"}, allEntries = true)
    public ApiResponse deleteVehicle(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId);
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

                if (row.length < 7) {
                    throw new InvalidInputException("Invalid CSV format. Expected 7 columns but got " + row.length);
                }

                String fullName = row[0];
                String email = row[1];

                User user = userRepository.findByEmail(email).orElseGet(() -> {
                    User newUser = new User();
                    newUser.setName(fullName);
                    newUser.setEmail(email);
                    return userRepository.save(newUser);
                });

                Vehicle vehicle = new Vehicle();
                vehicle.setRegNumber(row[2]);
                vehicle.setBrand(row[3]);
                vehicle.setModel(row[4]);
                vehicle.setInsuranceExpiryDate(row[5]);
                vehicle.setServiceDueDate(row[6]);
                vehicle.setUser(user);

                vehicleRepository.save(vehicle);
            }

        } catch (InvalidInputException e) {
            throw e; // Re-throw to be handled by GlobalExceptionHandler
        } catch (Exception e) {
            throw new InvalidInputException("Error reading CSV: " + e.getMessage());
        }
    }
}
