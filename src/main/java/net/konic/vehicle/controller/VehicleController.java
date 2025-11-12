package net.konic.vehicle.controller;

import net.konic.vehicle.dto.ApiResponse;
import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicle")
@CrossOrigin("*")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.createVehicle(vehicle));
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.deleteVehicle(id));
    }

    // ---------------- DASHBOARD ----------------
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalUsers", vehicleService.getTotalUsers());
        dashboard.put("totalVehicles", vehicleService.getTotalVehicles());
        return ResponseEntity.ok(dashboard);
    }
}
