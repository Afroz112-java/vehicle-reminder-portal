package net.konic.vehicle.controller;

import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;


    // 1. Endpoint to add a new vehicle
    @PostMapping("/addVehicle")
    public Vehicle addVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.addVehicle(vehicle);
    }

    // 2.  Endpoint to retrieve all vehicles+
    @GetMapping("/fetchAll")
    public List<Vehicle> getAll() {
        return vehicleService.getAllVehicles();
    }

    // 3. Endpoint to retrieve a vehicle by ID
    @GetMapping("/{id}")
    public Vehicle getVehicleById(@RequestParam("vehicleId") Long vehicleId) {
        return vehicleService.getVehicleById(vehicleId);
    }

    // 4. Endpoint to update a vehicle
    @PutMapping("/{id}")
    public Vehicle updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicleDetails) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        if (vehicle != null) {
            vehicle.setRegNumber(vehicleDetails.getRegNumber());
            vehicle.setBrand(vehicleDetails.getBrand());
            vehicle.setModel(vehicleDetails.getModel());
            vehicle.setInsuranceExpiryDate(vehicleDetails.getInsuranceExpiryDate());
            vehicle.setServiceDueDate(vehicleDetails.getServiceDueDate());
            return vehicleService.updateVehicle(id, vehicle);
        } else {
            return null;

        }
    }
    //5.Endpoint to Delete a vehicle
    @DeleteMapping("{id}")
    public String deleteVehicleById(@PathVariable Long id) {
         vehicleService.deleteVehicle(id);
         return "Vehicle deleted";
    }
}
