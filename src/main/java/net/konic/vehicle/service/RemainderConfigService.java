package net.konic.vehicle.service;

import lombok.RequiredArgsConstructor;
import net.konic.vehicle.entity.ReminderConfig;
import net.konic.vehicle.repository.ReminderConfigRepository;
import org.springframework.stereotype.Service;

/**
 * Service class that acts as a bridge between the Controller and Repository.
 * Why do you need this class?
 * ---------------------------
 * 1. Scheduler reads the reminder configuration from here.
 * 2. Controller uses this to update the cron expression and reminder days.
 * 3. Business logic (like always using ID=1) is written here.
 * 4. Keeps your code clean and avoids writing logic inside controllers.
 */
@Service  //Marks the class as a service component so Spring can detect it and manage it as a business-logic bean.
@RequiredArgsConstructor //Automatically generates a constructor for all final fields, enabling clean and safe dependency injection
public class RemainderConfigService {
    /**
     * Fetches the reminder configuration from the database.
     * Why fetch ID=1?
     * ----------------
     * Because the project uses ONLY ONE configuration row.
     * ID = 1 will always store:
     *   - serviceDaysBefore
     *   - insuranceDaysBefore
     *   - cronExpression
     * The scheduler reads this configuration dynamically.
     */
    private final ReminderConfigRepository repository;
    public ReminderConfig getConfig() {
        return repository.findById(1L).orElseThrow(
                () -> new RuntimeException("ReminderConfig with ID=1 not found!")
        );
    }

    /**
     * Saves (updates) the reminder configuration.
     * Important:
     * ----------
     * We always update the SAME ROW (ID = 1)
     * So the database will have only a single configuration.
     * This ensures:
     *   - No duplicate configs
     *   - Scheduler always uses the same config
     */
    public ReminderConfig save(ReminderConfig config) {
        config.setId(1L); // Always update row 1
        return repository.save(config);
    }
}
