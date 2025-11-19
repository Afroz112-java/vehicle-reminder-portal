package net.konic.vehicle.controller;

import net.konic.vehicle.Schedular.RemainderScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reminders")  // BASE URL
public class ReminderController {

    @Autowired
    private RemainderScheduler remainderScheduler;

    // 1️⃣ Get Reminder Details by Audit ID
    @GetMapping("/{id}")
    public ResponseEntity<String> getReminderDetails(@PathVariable Long id) {
        String result = remainderScheduler.getReminderDetails(id);
        return ResponseEntity.ok(result);
    }

    // 2️⃣ Trigger All Missing Reminders Manually
    @PostMapping("/trigger")
    public ResponseEntity<?> triggerAll() {
        try {
            remainderScheduler.sendAllReminders(); // THIS calls your scheduler logic 💡
            return ResponseEntity.ok("Manual Trigger DONE. Scheduler executed.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Trigger FAILED: " + e.getMessage());
        }
    }

    // 3️⃣ Send Reminder for ONE VEHICLE ID
    @PostMapping("/send/{id}")
    public ResponseEntity<?> sendReminderForVehicle(@PathVariable Long id) {
        boolean result = remainderScheduler.sendReminderByIdSafely(id);
        return ResponseEntity.ok(
                result ? "Reminder sent successfully." : "Reminder FAILED or not due."
        );
    }

    // 4️⃣ Send ONLY missing reminders
    @PostMapping("/send-missing")
    public ResponseEntity<?> sendMissingReminders() {
        int count = remainderScheduler.sendMissingRemindersSafely();
        return ResponseEntity.ok("Missing reminders sent: " + count);
    }

    // 5️⃣ Export Audit Report to csv
    @GetMapping("/export")
    public ResponseEntity<?> exportRemindersCSV() {
        byte[] file = remainderScheduler.exportReminderCSV();

        if (file.length == 0) {
            return ResponseEntity.badRequest().body("No data found to export");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reminders.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(file);
    }
}
