package net.konic.vehicle.controller;

import net.konic.vehicle.dto.ApiResponse;
import net.konic.vehicle.entity.UserEntity;
import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/vehicle")
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ---------------- USER MANAGEMENT ----------------

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    // ✅ Create user with vehicles
    @PostMapping("/user")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        return ResponseEntity.ok(adminService.createUser(user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteUser(id));
    }

    // ---------------- VEHICLE MANAGEMENT ----------------

    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(adminService.getAllVehicles());
    }

    @GetMapping("/vehicle/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getVehicleById(id));
    }

    // ✅ Create Vehicle (optional — can link to existing user by ID)
    @PostMapping()
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(adminService.createVehicle(vehicle));
    }

    @DeleteMapping("/vehicle/{id}")
    public ResponseEntity<ApiResponse> deleteVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteVehicle(id));
    }

    // ---------------- DASHBOARD SUMMARY ----------------

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalUsers", adminService.getTotalUsers());
        dashboard.put("totalVehicles", adminService.getTotalVehicles());
        return ResponseEntity.ok(dashboard);
    }
}
