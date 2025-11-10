package net.konic.vehicle.service;

import net.konic.vehicle.dto.ApiResponse;
import net.konic.vehicle.entity.UserEntity;
import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.repository.UserRepository;
import net.konic.vehicle.repository.VehicleRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public AdminService(UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    // ---------------- USER MANAGEMENT ----------------

    @Cacheable("users")
    public List<UserEntity> getAllUsers() {
        System.out.println("Fetching users from DB...");
        return userRepository.findAll();
    }

    @Cacheable(value = "user", key = "#userId")
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }

    @CacheEvict(value = {"users", "user"}, allEntries = true)
    public UserEntity createUser(UserEntity user) {
        // link user ↔ vehicles
        if (user.getVehicles() != null) {
            user.getVehicles().forEach(v -> v.setUser(user));
        }
        return userRepository.save(user);
    }

    @CacheEvict(value = {"users", "user"}, allEntries = true)
    public ApiResponse deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            return new ApiResponse(false, "User not found: " + userId);
        }
        userRepository.deleteById(userId);
        return new ApiResponse(true, "User deleted successfully");
    }

    // ---------------- VEHICLE MANAGEMENT ----------------

    @Cacheable("vehicles")
    public List<Vehicle> getAllVehicles() {
        System.out.println("Fetching vehicles from DB...");
        return vehicleRepository.findAll();
    }

    @Cacheable(value = "vehicle", key = "#vehicleId")
    public Vehicle getVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found: " + vehicleId));
    }

    @CacheEvict(value = {"vehicles", "vehicle"}, allEntries = true)
    public Vehicle createVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @CacheEvict(value = {"vehicles", "vehicle"}, allEntries = true)
    public ApiResponse deleteVehicle(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            return new ApiResponse(false, "Vehicle not found: " + vehicleId);
        }
        vehicleRepository.deleteById(vehicleId);
        return new ApiResponse(true, "Vehicle deleted successfully");
    }

    // ---------------- DASHBOARD ----------------

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalVehicles() {
        return vehicleRepository.count();
    }
}
