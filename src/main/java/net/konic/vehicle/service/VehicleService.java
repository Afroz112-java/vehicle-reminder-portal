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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
        if (vehicle .getUser() == null || vehicle.getUser().getEmail() == null) {
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] row;
            boolean header = true;

            while ((row = reader.readNext()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                // ✅ Expecting 8 columns: Full Name, Email, Phone, Reg Num, Brand, Model, Insurance, Service Due
                if (row.length < 8) {
                    throw new InvalidInputException("Invalid CSV format. Expected 8 columns but got " + row.length);
                }

                String fullName = row[0];
                String email = row[1];
                String phone = row[2];
                String regNumber = row[3];
                String brand = row[4];
                String model = row[5];
                String insuranceExpiry = row[6];
                String serviceDue = row[7];

                // ✅ Find or create user
                User user = userRepository.findByEmail(email).orElseGet(() -> {
                    User newUser = new User();
                    newUser.setName(fullName);
                    newUser.setEmail(email);
                    newUser.setPhone(phone);
                    return userRepository.save(newUser);
                });

                // ✅ Update phone if user exists but phone is missing
                if (user.getPhone() == null && phone != null && !phone.isEmpty()) {
                    user.setPhone(phone);
                    userRepository.save(user);
                }

                // ✅ Check if vehicle already exists
                Optional<Vehicle> existingVehicleOpt = vehicleRepository.findByRegNumber(regNumber);

                if (existingVehicleOpt.isPresent()) {
                    // Already in DB — skip adding again
                    System.out.println("Vehicle with regNumber " + regNumber + " already exists. Skipping insert.");
                    continue;
                }

                // ✅ Create and save vehicle
                Vehicle vehicle = new Vehicle();
                vehicle.setRegNumber(regNumber);
                vehicle.setBrand(brand);
                vehicle.setModel(model);
                vehicle.setInsuranceExpiryDate(LocalDate.parse(insuranceExpiry, formatter));
                vehicle.setServiceDueDate(LocalDate.parse(serviceDue, formatter));
                vehicle.setUser(user);

                vehicleRepository.save(vehicle);
            }

        } catch (InvalidInputException e) {
            throw e; // Let the global handler catch it
        } catch (Exception e) {
            throw new InvalidInputException("Error reading CSV: " + e.getMessage());
        }
    }

}


