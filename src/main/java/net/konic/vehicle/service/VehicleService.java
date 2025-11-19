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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public List<Vehicle> getByType(VehicleType type) {
        return vehicleRepository.findByVehicleType(type);
    }

    public List<Vehicle> getUserVehiclesByType(Long userId, VehicleType type) {
        return vehicleRepository.findByUserIdAndVehicleType(userId, type);
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

    // ---------------------------------------------------------
    // 1️⃣ FILE VALIDATION (Extension, MIME type, empty file)
    // ---------------------------------------------------------
    private void validateCsvFile(MultipartFile file) {

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
            throw new InvalidInputException("Only CSV files are allowed.");
        }

        String contentType = file.getContentType();
        if (contentType != null &&
                !contentType.equals("text/csv") &&
                !contentType.equals("application/vnd.ms-excel")) {
            throw new InvalidInputException("Invalid file format. Please upload a CSV file.");
        }

        if (file.isEmpty() || file.getSize() == 0) {
            throw new InvalidInputException("Uploaded file is empty.");
        }
    }

    // ---------------------------------------------------------
    // 2️⃣ ROW VALIDATION (column count)
    // ---------------------------------------------------------
    private void validateRowStructure(String[] row, int expectedColumns) {
        if (row.length < expectedColumns) {
            throw new InvalidInputException(
                    "Invalid CSV format. Expected " + expectedColumns + " columns but got " + row.length
            );
        }
    }

    // ---------------------------------------------------------
    // 3️⃣ HANDLE USER
    // ---------------------------------------------------------
    private User handleUser(String fullName, String email, String phone) {

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setName(fullName);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            return userRepository.save(newUser);
        });

        if (user.getPhone() == null) {
            user.setPhone(phone);
            userRepository.save(user);
        }

        return user;
    }

    // ---------------------------------------------------------
    // 4️⃣ HANDLE VEHICLE
    // ---------------------------------------------------------
    private void handleVehicle(User user,
                               String regNumber,
                               String brand,
                               String model,
                               LocalDate insuranceDate,
                               LocalDate serviceDueDate,
                               VehicleType vehicleType) {

        if (vehicleRepository.findByRegNumber(regNumber).isPresent()) {
            System.out.println("Vehicle " + regNumber + " already exists. Skipping.");
            return;
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setRegNumber(regNumber);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setInsuranceExpiryDate(insuranceDate);
        vehicle.setServiceDueDate(serviceDueDate);
        vehicle.setUser(user);
        vehicle.setInsuranceReminderSent(Boolean.FALSE);
        vehicle.setServiceReminderSent(Boolean.FALSE);
        vehicle.setVehicleType(vehicleType);


        vehicleRepository.save(vehicle);
    }

    // ---------------------------------------------------------
    // 5️⃣ MAIN METHOD (super clean)
    // ---------------------------------------------------------
    public void saveUserAndVehiclesFromCsv(MultipartFile file) {

        // Step 1: Validate the CSV file
        validateCsvFile(file);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {

            String[] row;
            boolean header = true;

            while ((row = reader.readNext()) != null) {

                if (header) {
                    header = false;
                    continue;
                }

                // Step 2: Validate number of columns (now 11)
                validateRowStructure(row, 11);

                // Step 3: Validate each field carefully
                String fullName = CsvValidationUtils.required(row[0], "Full Name");
                String email = CsvValidationUtils.validateEmail(row[1]);
                String phone = CsvValidationUtils.validatePhone(row[2]);
                String regNumber = CsvValidationUtils.required(row[3], "Registration Number");
                String brand = CsvValidationUtils.required(row[4], "Brand");
                String model = CsvValidationUtils.required(row[5], "Model");

                LocalDate insuranceDate =
                        CsvValidationUtils.validateDate(row[6], "Insurance Expiry", formatter);

                LocalDate serviceDueDate =
                        CsvValidationUtils.validateDate(row[7], "Service Due Date", formatter);

                // ⭐ Vehicle Type is at column index 10
                VehicleType vehicleType = VehicleType.valueOf(row[10].trim().toUpperCase());

                // Step 4: Save or update user
                User user = handleUser(fullName, email, phone);

                // Step 5: Save vehicle with type
                handleVehicle(user, regNumber, brand, model, insuranceDate, serviceDueDate, vehicleType);
            }

        } catch (InvalidInputException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidInputException("Error reading CSV: " + e.getMessage());
        }
    }
}
