package net.konic.vehicle.controller;

import net.konic.vehicle.Schedular.RemainderScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reminder")
public class ReminderController {

    private final RemainderScheduler remainderScheduler;

    public ReminderController(RemainderScheduler remainderScheduler) {
        this.remainderScheduler = remainderScheduler;
    }

    // 1️⃣ Trigger send all reminders manually
    @PostMapping("/send-all")
    public ResponseEntity<String> sendAllReminders() {
        remainderScheduler.sendAllReminders();
        return ResponseEntity.ok("All reminders sent successfully");
    }

    // 2️⃣ Send reminder for one particular vehicle ID
    @PostMapping("/send/{id}")
    public ResponseEntity<Map<String, Object>> sendReminderById(@PathVariable Long id) {
        boolean sent = remainderScheduler.sendReminderById(id);

        return ResponseEntity.ok(
                Map.of(
                        "vehicleId", id,
                        "status", sent ? "SUCCESS" : "FAILED"
                )
        );
    }

    // 3️⃣ Get reminder audit details by reminderAudit ID
    @GetMapping("/details/{id}")
    public ResponseEntity<String> getReminderDetails(@PathVariable Long id) {
        String details = remainderScheduler.getReminderDetails(id);
        return ResponseEntity.ok(details);
    }
}
