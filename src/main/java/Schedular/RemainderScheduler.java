package Schedular;

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

@Component
@Slf4j
public class RemainderScheduler     {

    private final VehicleRepository vehicleRepository;

    private final EmailService emailService;

    private final VehicleService vehicleService;

    @Value("${reminder.service.daysBefore}")
    private int serviceDaysBefore;
    @Value("${reminder.insurance.daysBefore}")
    private int insuranceDaysBefore;

    public RemainderScheduler(EmailService emailService, VehicleRepository vehicleRepository, VehicleService vehicleService) {
        this.emailService = emailService;
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = vehicleService;
    }


    @Scheduled(cron = " ${reminder.scheduler.cron}")// Every day at 9 AM
    public void sendReminders() {
        log.info("Reminder Scheduler started " + Instant.now());

        // Calculate target dates
        LocalDate today = LocalDate.now();
        LocalDate serviceTarget = today.plusDays(serviceDaysBefore);
        LocalDate insuranceTarget = today.plusDays(insuranceDaysBefore);




        //Fetch from DB
        List<Vehicle> serviceVehicles = vehicleRepository.findVehiclesForServiceReminder(serviceTarget.atStartOfDay());
        List<Vehicle> insuranceVehicles = vehicleRepository.findVehiclesForInsuranceReminder(insuranceTarget.atStartOfDay());

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
        String body = "Dear " + vehicle.getUser() + ",\n\n" +
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
        String subject = " Insurance Reminder";
        String body = "Dear " + vehicle.getUser().getEmail() + ",\n\n" +
                "Your vehicle insurance for *" + vehicle.getRegNumber() +
                "* will expire on " + vehicle.getInsuranceExpiryDate() +
                ". Please renew soon.\n\nRegards,\nVehicle Reminder System";

        sendEmail(vehicle, subject, body);
    }
    //  Common Email Sending Logic
    private void sendEmail(Vehicle vehicle, String subject, String body) {
        if (vehicle.getUser().getEmail() != null) {
            ReminderDTO dto = new ReminderDTO(vehicle.getUser().getEmail(), subject, body);
            emailService.sendEmail(dto);
            log.info("Email sent to: " + vehicle.getUser().getEmail() + " | " + subject);
        } else {
            log.warn(" No email found for " + vehicle.getRegNumber());
        }
    }

}
