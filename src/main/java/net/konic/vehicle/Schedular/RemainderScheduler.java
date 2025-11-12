package net.konic.vehicle.Schedular;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.konic.vehicle.Email.EmailService;
import net.konic.vehicle.dto.ReminderDTO;
import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.repository.VehicleRepository;
import net.konic.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component // Marks this class as a Spring Bean so it runs automatically
@Slf4j // Lombok annotation — gives us a "log" object for logging info
public class RemainderScheduler     {

    // Repositories and services needed for this class
    private final VehicleRepository vehicleRepository;
    private final EmailService emailService;
    private final VehicleService vehicleService;

    // Values taken from application.properties file
    @Value("${reminder.service.daysBefore}")
    private int serviceDaysBefore;
    @Value("${reminder.insurance.daysBefore}")
    private int insuranceDaysBefore;

    // Constructor — used by Spring to inject required dependencies
    public RemainderScheduler(EmailService emailService, VehicleRepository vehicleRepository, VehicleService vehicleService) {
        this.emailService = emailService;
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = vehicleService;
    }

    // This method runs automatically at a specific time (set in properties)
    @Scheduled(cron = "${reminder.scheduler.cron}")// Every day at 9 AM
    public void sendReminders() {
        log.info("Reminder Scheduler started " + Instant.now());// Logs current time


        LocalDate today = LocalDate.now();// Get today's date
        LocalDate serviceTarget = today.plusDays(serviceDaysBefore);
        LocalDate insuranceTarget = today.plusDays(insuranceDaysBefore);

        // Fetch vehicles from database whose service/insurance is near the target date
        List<Vehicle> serviceVehicles = vehicleRepository.findVehiclesForServiceReminder(serviceTarget.toString());
        List<Vehicle> insuranceVehicles = vehicleRepository.findVehiclesForInsuranceReminder(insuranceTarget.toString());

        log.info("✅ Scheduler completed. Service reminders: {}, Insurance reminders: {}",
                serviceVehicles.size(), insuranceVehicles.size());

        // To track vehicles that are already processed (to avoid duplicate emails)
        Set<Long> processedVehicleIds= new java.util.HashSet<>();



        for (Vehicle v : serviceVehicles) {
            if (insuranceVehicles.stream().anyMatch(i -> Objects.equals(i.getId(), v.getId()))) {
                // Same vehicle appears in both lists
                sendCombinedEmail(v);
                processedVehicleIds.add(v.getId());
            } else {
                sendServiceEmail(v);
            }
        }
        // 4️⃣ Send insurance-only reminders (those not already processed)
        for (Vehicle v : insuranceVehicles) {
            if (!processedVehicleIds.contains(v.getId())) {
                sendInsuranceEmail(v);
            }
        }
        log.info("✅ Scheduler done. Service: " + serviceVehicles.size() +
                ", Insurance: " + insuranceVehicles.size());
    }
    // ✉️ Combined Email
    private void sendCombinedEmail(Vehicle vehicle) {
        String subject = " Vehicle Reminder: Service & Insurance Due Soon";
        String body = "Dear " + vehicle.getUser().getName() + ",\n\n" +
                "This is a friendly reminder that your vehicle *" + vehicle.getRegNumber() + "*:\n" +
                "• Service is due on: " + vehicle.getServiceDueDate() + "\n" +
                "• Insurance expires on: " + vehicle.getInsuranceExpiryDate() + "\n\n" +
                "Please take necessary action to avoid inconvenience.\n\nRegards,\nVehicle Reminder System";

        sendEmail(vehicle, subject, body);
    }
    //Only Service
    private void sendServiceEmail(Vehicle vehicle) {
        String subject = " Service Reminder";
        String body = "Dear " + vehicle.getUser() + ",\n\n" +
                "Your vehicle *" + vehicle.getRegNumber() + "* is due for service on " +
                vehicle.getServiceDueDate() + ".\n\nRegards,\nVehicle Reminder System";

        sendEmail(vehicle, subject, body);
    }
    //Only Insurance
    private void sendInsuranceEmail(Vehicle vehicle) {
        String subject = "🛡️ Insurance Reminder";
        String body = "Dear " + vehicle.getUser() + ",\n\n" +
                "Your vehicle insurance for *" + vehicle.getRegNumber() +
                "* will expire on " + vehicle.getInsuranceExpiryDate() +
                ". Please renew soon.\n\nRegards,\nVehicle Reminder System";

        sendEmail(vehicle, subject, body);
    }
    //  Common Email Sending Logic
    private void sendEmail(Vehicle vehicle, String subject, String body) {
        if (vehicle.getUser() != null) {
            ReminderDTO dto = new ReminderDTO(vehicle.getUser().getEmail(), subject, body);
            emailService.sendEmail(dto);
            log.info("Email sent to: " + vehicle.getUser().getEmail() + " | " + subject);
        } else {
            log.warn(" No email found for " + vehicle.getRegNumber());
        }
    }

}
