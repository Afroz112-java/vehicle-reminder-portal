package net.konic.vehicle.Schedular;

import lombok.extern.slf4j.Slf4j;
import net.konic.vehicle.Email.EmailService;
import net.konic.vehicle.dto.ReminderDTO;
import net.konic.vehicle.entity.ReminderConfig;
import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.entity.ReminderAudit;
import net.konic.vehicle.repository.ReminderAuditRepository;
import net.konic.vehicle.repository.VehicleRepository;
import net.konic.vehicle.service.RemainderConfigService;
import net.konic.vehicle.service.VehicleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

@Component // Marks this class as a Spring-managed bean for dependency injection.
@Slf4j     // Automatically provides a Logger named 'log' for logging messages.
public class RemainderScheduler {
    /*
     * WHY WE NEED THIS SCHEDULER CLASS?
     * ----------------------------------
     *  This class runs automatically based on CRON time.
     *  It checks all vehicles in the database.
     *  Finds which vehicles need service or insurance reminders.
     *  Sends email to users.
     *  Stores email details in reminder_audit table.
     *  CRON timing is controlled from database (dynamic scheduling).
     */
    private final RemainderConfigService remainderConfigService;
    private final VehicleRepository vehicleRepository;
    private final EmailService emailService;
    private final VehicleService vehicleService;
    private final ReminderAuditRepository reminderAuditRepository;

    public RemainderScheduler(EmailService emailService,
                              VehicleRepository vehicleRepository,
                              VehicleService vehicleService,
                              ReminderAuditRepository reminderAuditRepository,
                              RemainderConfigService remainderConfigService) {

        this.remainderConfigService = remainderConfigService;
        this.reminderAuditRepository = reminderAuditRepository;
        this.emailService = emailService;
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = vehicleService;
    }
    // 1. MAIN SCHEDULER  → runs automatically
    //    CRON TIME IS DYNAMIC — COMING FROM DATABASE
    // --- DYNAMIC CRON EXPRESSION ---
    @Scheduled(cron = "#{remainderConfigService.getConfig().getCronExpression()}")
    public void sendReminders() {

        log.info("Reminder Scheduler started at {}", Instant.now());
        // Fetch serviceDaysBefore & insuranceDaysBefore from database table "reminder_config"
        ReminderConfig config = remainderConfigService.getConfig();
        int serviceDaysBefore = config.getServiceDaysBefore();
        int insuranceDaysBefore = config.getInsuranceDaysBefore();

        log.info("Loaded Config → Service: {} days, Insurance: {} days",
                serviceDaysBefore, insuranceDaysBefore);
        // Calculate dates for reminder
        LocalDate today = LocalDate.now();
        LocalDate serviceTarget = today.plusDays(serviceDaysBefore);
        LocalDate insuranceTarget = today.plusDays(insuranceDaysBefore);
        // Fetch vehicles that need reminders
        List<Vehicle> serviceVehicles = vehicleRepository.findVehiclesForServiceReminder(serviceTarget);
        List<Vehicle> insuranceVehicles = vehicleRepository.findVehiclesForInsuranceReminder(insuranceTarget);

        log.info("Vehicles → Service: {}, Insurance: {}",
                serviceVehicles.size(), insuranceVehicles.size());
        // To avoid sending duplicate reminders
        Set<Long> processedIds = new HashSet<>();

        // SERVICE + BOTH
        for (Vehicle v : serviceVehicles) {
            boolean hasInsurance = insuranceVehicles.stream()
                    .anyMatch(i -> Objects.equals(i.getId(), v.getId()));

            if (hasInsurance) {
                sendCombinedEmail(v);
                processedIds.add(v.getId());
            } else {
                sendServiceEmail(v);
            }
        }

        // INSURANCE ONLY
        for (Vehicle v : insuranceVehicles) {
            if (!processedIds.contains(v.getId())) {
                sendInsuranceEmail(v);
            }
        }

        log.info("Scheduler completed.");
    }

    // ------------------ EMAIL TEMPLATES ------------------

    private void sendCombinedEmail(Vehicle v) {
        sendEmail(v,
                "Vehicle Reminder: Service & Insurance Due",
                "Dear " + v.getUser().getName() + ",\n\n" +
                        "Your vehicle " + v.getRegNumber() + ":\n" +
                        "• Service due: " + v.getServiceDueDate() + "\n" +
                        "• Insurance expires: " + v.getInsuranceExpiryDate() + "\n\n" +
                        "Regards,\nVehicle Reminder System",
                "BOTH");
    }

    private void sendServiceEmail(Vehicle v) {
        sendEmail(v,
                "Service Reminder",
                "Dear " + v.getUser().getName() + ",\n\n" +
                        "Your vehicle " + v.getRegNumber() +
                        " is due for service on " + v.getServiceDueDate() + ".\n\n" +
                        "Regards,\nVehicle Reminder System",
                "SERVICE");
    }

    private void sendInsuranceEmail(Vehicle v) {
        sendEmail(v,
                "Insurance Reminder",
                "Dear " + v.getUser().getName() + ",\n\n" +
                        "Your vehicle insurance for " + v.getRegNumber() +
                        " will expire on " + v.getInsuranceExpiryDate() + ".\n\n" +
                        "Regards,\nVehicle Reminder System",
                "INSURANCE");
    }

    // -------------------- MAIN EMAIL LOGIC ---------------------

    private void sendEmail(Vehicle vehicle, String subject, String body, String type) {

        String email = vehicle.getUser() != null ? vehicle.getUser().getEmail() : null;

        // If no user email → DO NOT try to send email
        if (email == null || email.isEmpty()) {
            log.warn("Skipping {} reminder → No email for vehicle {}", type, vehicle.getRegNumber());
            auditReminder(vehicle, type, false, "No user email found");
            return;
        }

        try {
            // Attempt to send
            emailService.sendEmail(new ReminderDTO(email, subject, body));

            // UPDATE FLAGS
            if (type.equals("SERVICE")) {
                vehicle.setServiceReminderSent(true);
            } else if (type.equals("INSURANCE")) {
                vehicle.setInsuranceReminderSent(true);
            } else if (type.equals("BOTH")) {
                vehicle.setServiceReminderSent(true);
                vehicle.setInsuranceReminderSent(true);
            }

            vehicleRepository.save(vehicle);


            log.info("Email SUCCESS → {} | Vehicle: {}", type, vehicle.getRegNumber());
            auditReminder(vehicle, type, true, "Email sent successfully");

        } catch (Exception ex) {
            log.error("Email FAILED → {} | Vehicle: {} | Error: {}", type, vehicle.getRegNumber(), ex.getMessage());
            auditReminder(vehicle, type, false, ex.getMessage());
        }
    }

    // ---------------------- AUDIT -----------------------

    private void auditReminder(Vehicle v, String type, boolean success, String message) {
        // Stores email log into reminder_audit table
        ReminderAudit audit = ReminderAudit.builder()
                .userId(v.getUser() != null ? v.getUser().getId() : null)
                .vehicleRegNumber(v.getRegNumber())
                .reminderType(type)
                .email(v.getUser() != null ? v.getUser().getEmail() : null)
                .emailSent(success)
                .message(message)
                .sentAt(LocalDateTime.now())
                .build();

        reminderAuditRepository.save(audit);

        log.info("Audit → {} | Type: {} | Success: {}", v.getRegNumber(), type, success);
    }
}