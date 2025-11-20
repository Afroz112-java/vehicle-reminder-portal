package net.konic.vehicle.Schedular;

import lombok.extern.slf4j.Slf4j;
import net.konic.vehicle.Email.EmailService;
import net.konic.vehicle.dto.ReminderDTO;
import net.konic.vehicle.entity.ReminderConfig;
import net.konic.vehicle.entity.ReminderAudit;
import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.repository.ReminderAuditRepository;
import net.konic.vehicle.repository.VehicleRepository;
import net.konic.vehicle.service.RemainderConfigService;
import net.konic.vehicle.service.VehicleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Slf4j
public class RemainderScheduler {

    private final VehicleRepository vehicleRepository;
    private final EmailService emailService;
    private final VehicleService vehicleService;
    private final ReminderAuditRepository reminderAuditRepository;
    private final RemainderConfigService remainderConfigService;

    public RemainderScheduler(EmailService emailService,
                              VehicleRepository vehicleRepository,
                              VehicleService vehicleService,
                              ReminderAuditRepository reminderAuditRepository,
                              RemainderConfigService remainderConfigService) {
        this.emailService = emailService;
        this.vehicleRepository = vehicleRepository;
        this.vehicleService = vehicleService;
        this.reminderAuditRepository = reminderAuditRepository;
        this.remainderConfigService = remainderConfigService;
    }

    // -------------------- SCHEDULER -----------------------------
    @Scheduled(cron = "#{remainderConfigService.getConfig().getCronExpression()}")
    public void sendReminders() {
        log.info("Reminder Scheduler started at {}", Instant.now());

        ReminderConfig config = remainderConfigService.getConfig();
        int serviceDaysBefore = config.getServiceDaysBefore();
        int insuranceDaysBefore = config.getInsuranceDaysBefore();

        LocalDate today = LocalDate.now();
        LocalDate serviceTarget = today.plusDays(serviceDaysBefore);
        LocalDate insuranceTarget = today.plusDays(insuranceDaysBefore);

        List<Vehicle> serviceVehicles =
                vehicleRepository.findVehiclesForServiceReminder(serviceTarget);

        List<Vehicle> insuranceVehicles =
                vehicleRepository.findVehiclesForInsuranceReminder(insuranceTarget);

        Set<Long> processed = new HashSet<>();

        // SERVICE
        for (Vehicle v : serviceVehicles) {
            boolean inInsurance = insuranceVehicles.stream()
                    .anyMatch(i -> Objects.equals(i.getId(), v.getId()));

            if (inInsurance) {  // send whichever is earlier date
                if (v.getServiceDueDate().isBefore(v.getInsuranceExpiryDate())) {
                    sendServiceEmail(v);
                } else {
                    sendInsuranceEmail(v);
                }
            } else {
                sendServiceEmail(v);
            }
            processed.add(v.getId());
        }

        // INSURANCE
        for (Vehicle v : insuranceVehicles) {
            if (!processed.contains(v.getId())) {
                sendInsuranceEmail(v);
            }
        }

        log.info("Scheduler completed.");
    }

    // -------------------- EMAIL SENDING -------------------------

    private void sendServiceEmail(Vehicle vehicle) {

        if (emailAlreadySentToday(vehicle.getRegNumber(), "SERVICE")) {
            log.info("Skipping duplicate SERVICE email for {}", vehicle.getRegNumber());
            return;
        }

        String subject = "Service Reminder";
        String body =
                "Dear " + getUserName(vehicle) + ",\n\n" +
                        "Your vehicle " + vehicle.getRegNumber() + " is due for service on " +
                        vehicle.getServiceDueDate() + ".\n\nRegards,\nVehicle Reminder System";

        sendEmail(vehicle, subject, body, "SERVICE");
    }

    public int sendMissingRemindersSafely() {

        List<Vehicle> vehicles = vehicleRepository.findAll();
        int count = 0;

        for (Vehicle v : vehicles) {

            // Fetch last audit entry for this vehicle
            Optional<ReminderAudit> lastAudit =
                    reminderAuditRepository.findTopByVehicleRegNumberOrderBySentAtDesc(v.getRegNumber());

            // If no audit exists → missing reminder → send again
            if (lastAudit.isEmpty()) {

                // Choose type dynamically
                if (v.getServiceDueDate() != null &&
                        v.getServiceDueDate().isBefore(LocalDate.now().plusDays(7))) {

                    sendServiceEmail(v);
                    count++;
                    continue;
                }

                if (v.getInsuranceExpiryDate() != null &&
                        v.getInsuranceExpiryDate().isBefore(LocalDate.now().plusDays(7))) {

                    sendInsuranceEmail(v);
                    count++;
                }
            }
        }

        return count;
    }

    private void sendInsuranceEmail(Vehicle vehicle) {

        if (emailAlreadySentToday(vehicle.getRegNumber(), "INSURANCE")) {
            log.info("Skipping duplicate INSURANCE email for {}", vehicle.getRegNumber());
            return;
        }

        String subject = "Insurance Reminder";
        String body =
                "Dear " + getUserName(vehicle) + ",\n\n" +
                        "Your vehicle insurance for " + vehicle.getRegNumber() +
                        " will expire on " + vehicle.getInsuranceExpiryDate() + ".\n\n" +
                        "Regards,\nVehicle Reminder System";

        sendEmail(vehicle, subject, body, "INSURANCE");
    }

    private void sendEmail(Vehicle vehicle, String subject, String body, String type) {
        try {
            if (vehicle.getUser() != null && vehicle.getUser().getEmail() != null) {

                emailService.sendEmail(new ReminderDTO(
                        vehicle.getUser().getEmail(), subject, body
                ));

                saveAudit(vehicle, type, true, "Email sent");

                log.info("Email sent → {} [{}]", vehicle.getRegNumber(), type);
            } else {
                saveAudit(vehicle, type, false, "Email not found");
            }
        } catch (Exception e) {
            saveAudit(vehicle, type, false, e.getMessage());
            log.error("Failed to send email to {}", vehicle.getRegNumber(), e);
        }
    }

    // -------------------- AUDIT -------------------------

    private void saveAudit(Vehicle vehicle, String type, boolean success, String msg) {
        ReminderAudit audit = new ReminderAudit();
        audit.setVehicleRegNumber(vehicle.getRegNumber());
        audit.setReminderType(type);
        audit.setEmail(vehicle.getUser() != null ? vehicle.getUser().getEmail() : null);
        audit.setEmailSent(success);
        audit.setMessage(msg);
        audit.setSentAt(LocalDateTime.now());

        reminderAuditRepository.save(audit);
    }

    private boolean emailAlreadySentToday(String regNumber, String type) {
        return reminderAuditRepository
                .existsByVehicleRegNumberAndReminderTypeAndSentAtAfter(
                        regNumber, type, LocalDate.now().atStartOfDay());
    }

    // -------------------- UTIL -------------------------

    private String getUserName(Vehicle vehicle) {
        if (vehicle.getUser() != null && vehicle.getUser().getName() != null) {
            return vehicle.getUser().getName();
        }
        return "User";
    }

    public String getReminderDetails(Long auditId) {
        return reminderAuditRepository.findById(auditId)
                .map(Object::toString)
                .orElse("No Reminder Audit Found for ID = " + auditId);
    }


    // ===================== CONTROLLER HELPER METHODS =========================

    public void sendAllReminders() {
        sendReminders();
    }

    public boolean sendReminderById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id).orElse(null);
        if (vehicle == null) return false;

        sendServiceEmail(vehicle);
        sendInsuranceEmail(vehicle);

        return true;
    }

    public byte[] exportReminderCSV() {
        List<ReminderAudit> audits = reminderAuditRepository.findAll();
        if (audits.isEmpty()) return new byte[0];

        StringBuilder csv = new StringBuilder();
        csv.append("ID,Vehicle,Type,Email,Sent,Message,Date\n");

        for (ReminderAudit a : audits) {
            csv.append(a.getId()).append(",")
                    .append(a.getVehicleRegNumber()).append(",")
                    .append(a.getReminderType()).append(",")
                    .append(a.getEmail()).append(",")
                    .append(a.isEmailSent()).append(",")
                    .append(a.getMessage()).append(",")
                    .append(a.getSentAt()).append("\n");
        }

        return csv.toString().getBytes();
    }
}
