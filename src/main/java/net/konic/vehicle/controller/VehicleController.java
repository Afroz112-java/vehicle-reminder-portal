package net.konic.vehicle.controller;

import net.konic.vehicle.dto.ApiResponse;
import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
// This is the Controller class for Vehicle

@RestController
//  Handles HTTP requests for Vehicle-related operations
@RequestMapping("/api/vehicle")
//  Base URL mapping for Vehicle API endpoints
//  Allows cross-origin requests from any domain
@CrossOrigin("*")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // Create vehicle
    //  Handles HTTP POST requests to create a new vehicle
    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        //@RequestBody binds the HTTP request body to a Vehicle object
        return ResponseEntity.ok(vehicleService.createVehicle(vehicle));
    }

    // Get all vehicles
    // Handles HTTP GET requests to retrieve all vehicles
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    // Get by ID
    // Handles HTTP GET requests to retrieve a vehicle by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        //@pathVariable binds the vehicle ID from the URL to the method parameter
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    // Update vehicle
    // Handles HTTP PUT requests to update an existing vehicle

    @PutMapping("/{id}")
    //  Updates the vehicle with the specified ID using the provided vehicle data
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicle));
    }

    // Delete vehicle
    // Handles HTTP DELETE requests to delete a vehicle by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.deleteVehicle(id));
    }

    // Dashboard summary
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalVehicles", vehicleService.getTotalVehicles());
        return ResponseEntity.ok(dashboard);
    }

    // Upload CSV
    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        vehicleService.saveUserAndVehiclesFromCsv(file);
        return ResponseEntity.ok("CSV Uploaded Successfully");
    }
}