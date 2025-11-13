package net.konic.vehicle.Schedular;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.konic.vehicle.Email.EmailService;
import net.konic.vehicle.dto.ReminderDTO;
import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.entity.ReminderAudit;
import net.konic.vehicle.repository.ReminderAuditRepository;
import net.konic.vehicle.repository.VehicleRepository;
import net.konic.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@Slf4j
public class RemainderScheduler {

    private final VehicleRepository vehicleRepository;
    private final EmailService emailService;
    private final VehicleService vehicleService;
    private final ReminderAuditRepository reminderAuditRepository;

    @Value("${reminder.service.daysBefore}")
    private int serviceDaysBefore;

    @Value("${reminder.insurance.daysBefore}")
    private int insuranceDaysBefore;

    public RemainderScheduler(EmailService emailService,
                              VehicleRepository vehicleRepository,
                              VehicleService vehicleService,
                              ReminderAuditRepository reminderAuditRepository) {
        this.reminderAuditRepository = reminderAuditRepository;
        this.emailService = emailService;
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = vehicleService;
    }

    @Scheduled(cron = "${reminder.scheduler.cron}")
    public void sendReminders() {
        log.info("Reminder Scheduler started at {}", Instant.now());

        LocalDate today = LocalDate.now();
        LocalDate serviceTarget = today.plusDays(serviceDaysBefore);
        LocalDate insuranceTarget = today.plusDays(insuranceDaysBefore);

        List<Vehicle> serviceVehicles = vehicleRepository.findVehiclesForServiceReminder(serviceTarget);
        List<Vehicle> insuranceVehicles = vehicleRepository.findVehiclesForInsuranceReminder(insuranceTarget);

        log.info(" Scheduler fetched vehicles — Service: {}, Insurance: {}",
                serviceVehicles.size(), insuranceVehicles.size());

        Set<Long> processedVehicleIds = new java.util.HashSet<>();

        for (Vehicle v : serviceVehicles) {
            if (insuranceVehicles.stream().anyMatch(i -> Objects.equals(i.getId(), v.getId()))) {
                sendCombinedEmail(v);
                processedVehicleIds.add(v.getId());
            } else {
                sendServiceEmail(v);
            }
        }

        for (Vehicle v : insuranceVehicles) {
            if (!processedVehicleIds.contains(v.getId())) {
                sendInsuranceEmail(v);
            }
        }

        log.info(" Scheduler completed successfully.");
    }

    // --- EMAIL TEMPLATES ---

    private void sendCombinedEmail(Vehicle vehicle) {
        String subject = "Vehicle Reminder: Service & Insurance Due Soon";
        String body = "Dear " + vehicle.getUser().getName() + ",\n\n" +
                "This is a friendly reminder that your vehicle " + vehicle.getRegNumber() + ":\n" +
                "• Service due on: " + vehicle.getServiceDueDate() + "\n" +
                "• Insurance expires on: " + vehicle.getInsuranceExpiryDate() + "\n\n" +
                "Please take action to avoid inconvenience.\n\nRegards,\nVehicle Reminder System";

        sendEmail(vehicle, subject, body, "BOTH");
    }

    private void sendServiceEmail(Vehicle vehicle) {
        String subject = "Service Reminder";
        String body = "Dear " + vehicle.getUser().getName() + ",\n\n" +
                "Your vehicle " + vehicle.getRegNumber() + " is due for service on " +
                vehicle.getServiceDueDate() + ".\n\nRegards,\nVehicle Reminder System";

        sendEmail(vehicle, subject, body, "SERVICE");
    }

    private void sendInsuranceEmail(Vehicle vehicle) {
        String subject = " Insurance Reminder";
        String body = "Dear " + vehicle.getUser().getName() + ",\n\n" +
                "Your vehicle insurance for " + vehicle.getRegNumber() +
                " will expire on " + vehicle.getInsuranceExpiryDate() +
                ". Please renew soon.\n\nRegards,\nVehicle Reminder System";

        sendEmail(vehicle, subject, body, "INSURANCE");
    }

    // --- COMMON EMAIL LOGIC + AUDITING ---
    private void sendEmail(Vehicle vehicle, String subject, String body, String type) {
        try {
            if (vehicle.getUser() != null && vehicle.getUser().getEmail() != null) {
                ReminderDTO dto = new ReminderDTO(vehicle.getUser().getEmail(), subject, body);
                emailService.sendEmail(dto);
                log.info(" Email sent to: {} | Type: {}", vehicle.getUser().getEmail(), type);

                auditReminder(vehicle, type, true, "Email sent successfully");
            } else {
                log.warn("No email found for vehicle: {}", vehicle.getRegNumber());
                auditReminder(vehicle, type, false, "User email not found");
            }
        } catch (Exception e) {
            log.error(" Failed to send email for vehicle: {}", vehicle.getRegNumber(), e);
            auditReminder(vehicle, type, false, "Email sending failed: " + e.getMessage());
        }
    }


    // --- AUDIT HELPER METHOD ---
    private void auditReminder(Vehicle vehicle, String type, boolean success, String message) {
        ReminderAudit audit = ReminderAudit.builder()
                .userId(vehicle.getId())
                .vehicleRegNumber(vehicle.getRegNumber())
                .reminderType(type)
                .email(vehicle.getUser() != null ? vehicle.getUser().getEmail() : null)
                .emailSent(success)
                .message(message)
                .sentAt(LocalDateTime.now())
                .build();

        reminderAuditRepository.save(audit);
        log.info(" Audit record saved for {} | Type: {} | Success: {}", vehicle.getRegNumber(), type, success);
    }
}
