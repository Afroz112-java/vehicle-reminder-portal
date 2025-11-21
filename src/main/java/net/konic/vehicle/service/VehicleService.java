package net.konic.vehicle.service;

import com.opencsv.CSVReader;
import net.konic.vehicle.dto.ApiResponse;
import net.konic.vehicle.entity.User;
import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.entity.VehicleType;
import net.konic.vehicle.execption.InvalidInputException;
import net.konic.vehicle.execption.ResourceNotFoundException;
import net.konic.vehicle.repository.UserRepository;
import net.konic.vehicle.repository.VehicleRepository;
import net.konic.vehicle.utils.CsvValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(VehicleService.class);

    @Autowired
    private UserRepository userRepository;

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // ----------------------- CREATE VEHICLE -----------------------
    @CacheEvict(value = {"vehicles", "vehicle"}, allEntries = true)
    public Vehicle createVehicle(Vehicle vehicle) {

        if (vehicle == null) {
            throw new InvalidInputException("Vehicle must not be null.");
        }

        if (vehicle.getUser() == null ||
                vehicle.getUser().getEmail() == null ||
                vehicle.getUser().getEmail().isBlank()) {

            throw new InvalidInputException("User email must be provided to create a vehicle.");
        }

        String email = vehicle.getUser().getEmail().trim();

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = vehicle.getUser();
            newUser.setEmail(email);
            return userRepository.save(newUser);
        });

        vehicle.setUser(user);
        return vehicleRepository.save(vehicle);
    }

    // ----------------------- GET BY TYPE -----------------------
    public List<Vehicle> getByType(VehicleType type) {
        return vehicleRepository.findByVehicleType(type);
    }

    public List<Vehicle> getUserVehiclesByType(Long userId, VehicleType type) {
        return vehicleRepository.findByUserIdAndVehicleType(userId, type);
    }

    // ----------------------- GET ALL VEHICLES -----------------------
    @Cacheable("vehicles")
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            throw new ResourceNotFoundException("No vehicles found in the database.");
        }
        return vehicles;
    }

    // ----------------------- GET VEHICLE BY ID -----------------------
    @Cacheable(value = "vehicle", key = "#vehicleId")
    public Vehicle getVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId));
    }

    // ----------------------- UPDATE VEHICLE -----------------------
    @CacheEvict(value = {"vehicles", "vehicle"}, allEntries = true)
    public Vehicle updateVehicle(Long vehicleId, Vehicle updatedVehicle) {

        Vehicle existingVehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot update — Vehicle not found with ID: " + vehicleId));

        if (updatedVehicle.getRegNumber() != null) existingVehicle.setRegNumber(updatedVehicle.getRegNumber());
        if (updatedVehicle.getBrand() != null) existingVehicle.setBrand(updatedVehicle.getBrand());
        if (updatedVehicle.getModel() != null) existingVehicle.setModel(updatedVehicle.getModel());
        if (updatedVehicle.getInsuranceExpiryDate() != null) existingVehicle.setInsuranceExpiryDate(updatedVehicle.getInsuranceExpiryDate());
        if (updatedVehicle.getServiceDueDate() != null) existingVehicle.setServiceDueDate(updatedVehicle.getServiceDueDate());

        return vehicleRepository.save(existingVehicle);
    }

    // ----------------------- DELETE VEHICLE -----------------------
    @CacheEvict(value = {"vehicles", "vehicle"}, allEntries = true)
    public ApiResponse deleteVehicle(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId);
        }

        vehicleRepository.deleteById(vehicleId);
        return new ApiResponse(true, "Vehicle deleted successfully");
    }

    // ----------------------- DASHBOARD -----------------------
    public long getTotalVehicles() {
        return vehicleRepository.count();
    }

    // =========================================================================
    //                          HELPER METHODS (NO LOGIC CHANGE)
    // =========================================================================
    public void validateCsvFile(MultipartFile file) {

//        if (file == null || file.isEmpty()) {
//            throw new InvalidInputException("Uploaded file is empty.");
//        }
        if (file == null || file.getSize() == 0) {
            throw new InvalidInputException("Uploaded file is empty.");
        }


        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
            throw new InvalidInputException("Only CSV files are allowed.");
        }

        String contentType = file.getContentType();
        if (contentType != null &&
                !contentType.equals("text/csv") &&
                !contentType.equals("application/vnd.ms-excel") &&
                !contentType.equals("application/csv")) {

            throw new InvalidInputException("Invalid file format. Only CSV files are supported.");
        }
    }

    private void validateRowStructure(String[] row, int expectedColumns) {
        if (row.length < expectedColumns) {
            throw new InvalidInputException(
                    "Invalid CSV format. Expected " + expectedColumns + " columns but got " + row.length
            );
        }
    }

    private User handleUser(String fullName, String email, String phone) {

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setName(fullName);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            return userRepository.save(newUser);
        });

        if (user.getPhone() == null || user.getPhone().isBlank()) {
            user.setPhone(phone);
            userRepository.save(user);

        }

        return user;
    }

    private void handleVehicle(User user,
                               String regNumber,
                               String brand,
                               String model,
                               LocalDate insuranceDate,
                               LocalDate serviceDueDate,
                               VehicleType vehicleType) {

        if (vehicleRepository.findByRegNumber(regNumber).isPresent()) {
            log.info("Vehicle {} already exists. Skipping.", regNumber);
            return;
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setRegNumber(regNumber);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setInsuranceExpiryDate(insuranceDate);
        vehicle.setServiceDueDate(serviceDueDate);
        vehicle.setUser(user);
        vehicle.setVehicleType(vehicleType);
        vehicle.setInsuranceReminderSent(false);
        vehicle.setServiceReminderSent(false);

        vehicleRepository.save(vehicle);
    }

    // =========================================================================
    //                        MAIN CSV IMPORT METHOD
    // =========================================================================
    @CacheEvict(value = "vehicles", allEntries = true)
    public void saveUserAndVehiclesFromCsv(MultipartFile file) {

        validateCsvFile(file);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {

            String[] row;
            boolean header = true;
            boolean hasData = false;

            while ((row = reader.readNext()) != null) {

                if (header) {
                    header = false;
                    continue;
                }
                // Ignore empty lines
                if (row.length == 0 || (row[0] == null || row[0].isBlank())) {
                    continue;
                }

                validateRowStructure(row, 11);

                String fullName = CsvValidationUtils.required(row[0], "Full Name");
                String email = CsvValidationUtils.validateEmail(row[1]);
                String phone = CsvValidationUtils.validatePhone(row[2]);
                String regNumber = CsvValidationUtils.required(row[3], "Registration Number");
                String brand = CsvValidationUtils.required(row[4], "Brand");
                String model = CsvValidationUtils.required(row[5], "Model");

                LocalDate insuranceDate = CsvValidationUtils.validateDate(row[6], "Insurance Expiry", formatter);
                LocalDate serviceDueDate = CsvValidationUtils.validateDate(row[7], "Service Due Date", formatter);

                VehicleType vehicleType =
                        VehicleType.valueOf(row[10].trim().toUpperCase());

                User user = handleUser(fullName, email, phone);

                handleVehicle(user, regNumber, brand, model,
                        insuranceDate, serviceDueDate, vehicleType);
            }
            // ⭐ If no rows except header → throw error
            if (!hasData) {
                throw new InvalidInputException("CSV file contains no data.");
            }

        } catch (InvalidInputException e) {
            throw e;

        } catch (Exception e) {
            log.error("CSV processing error", e);
            throw new InvalidInputException("Error reading CSV: " + e.getMessage());
        }
    }
}
