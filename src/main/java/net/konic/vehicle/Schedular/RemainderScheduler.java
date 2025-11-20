package net.konic.vehicle.Schedular;

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

        log.info(" Scheduler fetched vehicles — Service: {}, Insurance: {}", serviceVehicles.size(), insuranceVehicles.size());

        Set<Long> processedVehicleIds = new java.util.HashSet<>();

        for (Vehicle v : serviceVehicles) {
            boolean inInsuranceList = insuranceVehicles.stream()
                    .anyMatch(i -> Objects.equals(i.getId(), v.getId()));

            if (inInsuranceList) {
                // Send only the reminder that expires sooner
                if (v.getServiceDueDate().isBefore(v.getInsuranceExpiryDate())) {
                    sendServiceEmail(v);
                } else {
                    sendInsuranceEmail(v);
                }
            } else {
                sendServiceEmail(v);
            }

            processedVehicleIds.add(v.getId()); // always mark as processed
        }

        for (Vehicle v : insuranceVehicles) {
            if (!processedVehicleIds.contains(v.getId())) {
                sendInsuranceEmail(v);
                processedVehicleIds.add(v.getId());
            }
        }

        log.info(" Scheduler completed successfully.");
    }

    // --- EMAIL TEMPLATES ---
    private void sendCombinedEmail(Vehicle vehicle) {
        String subject = "Vehicle Reminder: Service & Insurance Due Soon";
        String body = "Dear " + getSafeUserName(vehicle) + ",\n\n" +
                "This is a friendly reminder that your vehicle " + vehicle.getRegNumber() + ":\n" +
                "• Service due on: " + vehicle.getServiceDueDate() + "\n" +
                "• Insurance expires on: " + vehicle.getInsuranceExpiryDate() + "\n\n" +
                "Please take action to avoid inconvenience.\n\nRegards,\nVehicle Reminder System";

        sendEmail(vehicle, subject, body, "BOTH");
    }

    private void sendServiceEmail(Vehicle vehicle) {

        // 🚫 Prevent duplicate SERVICE mail in same day
        if (reminderAuditRepository.existsByVehicleRegNumberAndReminderTypeAndSentAtAfter(
                vehicle.getRegNumber(), "SERVICE", LocalDate.now().atStartOfDay())) {

            log.info("Service email already sent today. Skipping...");
            return;   // ❗ STOP HERE – no duplicate mail
        }

        String subject = "Service Reminder";
        String body = "Dear " + getSafeUserName(vehicle) + ",\n\n" +
                "Your vehicle " + vehicle.getRegNumber() + " is due for service on " +
                vehicle.getServiceDueDate() + ".\n\nRegards,\nVehicle Reminder System";

        sendEmail(vehicle, subject, body, "SERVICE"); // send + save audit
    }


    private void sendInsuranceEmail(Vehicle vehicle) {
        String subject = "Insurance Reminder";
        String body = "Dear " + getSafeUserName(vehicle) + ",\n\n" +
                "Your vehicle insurance for " + vehicle.getRegNumber() +
                " will expire on " + vehicle.getInsuranceExpiryDate() +
                ". Please renew soon.\n\nRegards,\nVehicle Reminder System";

        sendEmail(vehicle, subject, body, "INSURANCE");
    }


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

    // --- FIXED METHODS FOR CONTROLLER/SERVICE ---

    // Send all reminders manually
    public void sendAllReminders() {
        sendReminders(); // reuse existing scheduler logic
    }

    // Send reminder for a particular vehicle ID
    public boolean sendReminderById(Long id) {
        try {
            Vehicle vehicle = vehicleRepository.findById(id).orElse(null);
            if (vehicle == null) return false;

            // ❗ STOP if already sent today (avoid duplicate email)
            if (reminderAuditRepository.existsByVehicleRegNumberAndReminderTypeAndSentAtAfter(
                    vehicle.getRegNumber(),
                    "SERVICE",
                    LocalDate.now().atStartOfDay()
            )) {
                log.info("Reminder already sent today for vehicle: {}", vehicle.getRegNumber());
                return false;
            }


            LocalDate today = LocalDate.now();
            boolean sent = false;

            if (vehicle.getServiceDueDate() != null && !vehicle.getServiceDueDate().isAfter(today)) {
                sendServiceEmail(vehicle);
                sent = true;
            }
            if (vehicle.getInsuranceExpiryDate() != null && !vehicle.getInsuranceExpiryDate().isAfter(today)) {
                sendInsuranceEmail(vehicle);
                sent = true;
            }

            return sent;
        } catch (Exception e) {
            log.error("Failed to send reminder for vehicle ID: {}", id, e);
            return false;
        }
    }


    // Optional: get reminder details for audit
    public String getReminderDetails(Long id) {
        ReminderAudit audit = reminderAuditRepository.findFirstByVehicleIdOrderBySentAtDesc(id);

        if (audit == null) return "No reminder found for this ID";
        return "Reminder sent to " + audit.getEmail() + " | Type: " + audit.getReminderType();
    }
    // 🔁 REPLACE YOUR OLD METHOD WITH THIS
    public byte[] exportReminderCSV() {
        try {
            List<ReminderAudit> audits = reminderAuditRepository.findAll();
            if (audits.isEmpty()) {
                return new byte[0];  // No data to export
            }

            StringBuilder csv = new StringBuilder();

            // CSV Header
            csv.append("ID,Vehicle Reg Number,Type,Email,Email Sent,Message,Sent At\n");

            // CSV Rows
            for (ReminderAudit audit : audits) {
                csv.append(audit.getId()).append(",")
                        .append(audit.getVehicleRegNumber()).append(",")
                        .append(audit.getReminderType()).append(",")
                        .append(audit.getEmail()).append(",")
                        .append(audit.isEmailSent() ? "YES" : "NO").append(",")
                        .append(audit.getMessage()).append(",")
                        .append(audit.getSentAt().toString()).append("\n");
            }

            return csv.toString().getBytes();  // convert to byte array

        } catch (Exception e) {
            log.error("CSV Export FAILED", e);
            return new byte[0];
        }
    }




    public int sendMissingReminders() {
        // Placeholder for sending missing reminders logic
        return 0;
    }

    public boolean sendReminderByIdSafely(Long id) {
        return sendReminderById(id);
    }

    private String getSafeUserName(Vehicle vehicle) {
        if (vehicle.getUser() != null && vehicle.getUser().getName() != null) {
            return vehicle.getUser().getName();
        }
        return "Vehicle Owner"; // fallback if user is null
    }

    public int sendMissingRemindersSafely() {
        return sendMissingReminders();
    }
}