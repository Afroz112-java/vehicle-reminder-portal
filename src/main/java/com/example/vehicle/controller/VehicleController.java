package com.example.vehicle.controller;

import com.example.vehicle.entity.Vehicle;
import com.example.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VehicleController {

@Autowired
private VehicleService vehicleService;

    // Endpoint to add a new vehicle
    @PostMapping
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {
    return vehicleService.addVehicle(vehicle);
}
    // Endpoint to retrieve all vehicles+
    @GetMapping
    public List<Vehicle> getAll() {
        return vehicleService.getAllVehicles();
    }
   @GetMapping("/{id}")
    public Vehicle getVehicleById(@RequestParam("vehicleId") Long vehicleId) {
        return vehicleService.getVehicleById(vehicleId);
   }
}
