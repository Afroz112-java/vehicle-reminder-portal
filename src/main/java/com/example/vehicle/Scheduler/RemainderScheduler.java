package com.example.vehicle.Scheduler;

import com.example.vehicle.Email.EmailService;
import com.example.vehicle.entity.Vehicle;
import com.example.vehicle.repository.VehicleRepository;
import com.example.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RemainderScheduler {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VehicleRepository vehicleRepository;

    @Scheduled(cron = "1 * * * * *") // Runs every day at 9 AM
    public void sendMonthlyRemainders() {

        LocalDate today= LocalDate.now();

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        for(Vehicle vehicle : allVehicles){

            LocalDate serviceDate = vehicle.getServiceDueDate();
            if(serviceDate != null && today.isEqual(serviceDate.minusDays(30))){
                System.out.println("Reminder: Vehicle " + vehicle.getRegNumber() + " service is due on " + serviceDate + "(Owner: " + vehicle.getOwnerName() + ")");
            }

            LocalDate insuranceDate = vehicle.getInsuranceExpiryDate();
            if(insuranceDate != null && today.isEqual(insuranceDate.minusDays(30))){
                System.out.println("Reminder: Vehicle " + vehicle.getRegNumber() + " insurance is expiring on " + insuranceDate + "(Owner: " + vehicle.getOwnerName() + ")");

            }
        }
        System.out.println("Checked all vehicles for reminder on: " + today);


    }
}
