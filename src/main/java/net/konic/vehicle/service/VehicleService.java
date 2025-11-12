package net.konic.vehicle.service;

import net.konic.vehicle.dto.ApiResponse;
import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.repository.VehicleRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // ---------------- VEHICLE CRUD ----------------

    @CacheEvict(value = {"vehicles", "vehicle"}, allEntries = true)
    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Cacheable("vehicles")
    public List<Vehicle> getAllVehicles() {
        System.out.println("Fetching vehicles from DB...");
        return vehicleRepository.findAll();
    }

    @Cacheable(value = "vehicle", key = "#id")
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found: " + id));
    }

    @CacheEvict(value = {"vehicles", "vehicle"}, allEntries = true)
    public Vehicle updateVehicle(Long id, Vehicle newVehicle) {
        Vehicle existing = getVehicleById(id);
        existing.setRegNumber(newVehicle.getRegNumber());
        existing.setBrand(newVehicle.getBrand());
        existing.setModel(newVehicle.getModel());
        existing.setInsuranceExpiryDate(newVehicle.getInsuranceExpiryDate());
        existing.setServiceDueDate(newVehicle.getServiceDueDate());
        return vehicleRepository.save(existing);
    }

    @CacheEvict(value = {"vehicles", "vehicle"}, allEntries = true)
    public ApiResponse deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            return new ApiResponse(false, "Vehicle not found: " + id);
        }
        vehicleRepository.deleteById(id);
        return new ApiResponse(true, "Vehicle deleted successfully");
    }

    // ---------------- DASHBOARD ----------------
    public long getTotalVehicles() {
        return vehicleRepository.count();
    }

    public long getTotalUsers() {
        // Count distinct users by looping through vehicles
        return vehicleRepository.findAll().stream()
                .map(v -> v.getUser().getEmail())
                .distinct()
                .count();
    }
}
