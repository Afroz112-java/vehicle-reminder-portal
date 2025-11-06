package com.example.vehicle.controller;

import com.example.vehicle.entity.Vehicle;
import com.example.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class VehicleController {
@Autowired
private VehicleService vehicleService;
@PostMapping
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {
    return vehicleService.addVehicle(vehicle);
}
}
