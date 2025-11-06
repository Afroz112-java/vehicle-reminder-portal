package com.example.vehicle.service;

import com.example.vehicle.entity.Vehicle;
import com.example.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
   @Autowired
   private VehicleRepository vehicleRepository;

    public Vehicle addVehicle(Vehicle v) {
        return vehicleRepository.save(v);
    }
}
