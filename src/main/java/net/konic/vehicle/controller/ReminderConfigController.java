package net.konic.vehicle.controller;

import lombok.RequiredArgsConstructor;
import net.konic.vehicle.entity.ReminderConfig;
import net.konic.vehicle.service.RemainderConfigService;
import org.springframework.web.bind.annotation.*;
/**
 * REST Controller that allows the frontend/admin to:
 *   - GET the current reminder configuration
 *   - UPDATE the reminder configuration
 * Why do you need this controller?
 * --------------------------------
 * Because your scheduler reads values dynamically from the database.
 * To change scheduler timing or reminder days,
 * you need an API to update ReminderConfig (row ID = 1).
 * This controller exposes those APIs.
 */
@RestController  // Marks this class as a REST API controller that returns JSON responses
@RequestMapping("/api/reminder-config") // Sets the base URL path for all endpoints in this controller.
//@RequiredArgsConstructor
public class ReminderConfigController {

    private final RemainderConfigService service;

    public ReminderConfigController(RemainderConfigService service) {
        this.service = service;
    }
    /**
     * GET /api/reminder-config
     * Returns the current configuration stored in the database.
     * Used by admin dashboards or for debug.
     */
    @GetMapping
    public ReminderConfig getConfig() {
        return service.getConfig();
    }

//    /**
//     * PUT /api/reminder-config
//     * Updates the configuration (serviceDaysBefore, insuranceDaysBefore, cronExpression).
//     * Always updates row with ID = 1.
//     * Example JSON to send:
//     * {
//     *   "serviceDaysBefore": 10,
//     *   "insuranceDaysBefore": 20,
//     *   "cronExpression": "0 */5 * * * *"
//            * }
//     */
    @PutMapping
    public ReminderConfig updateConfig(@RequestBody ReminderConfig config) {
        return service.save(config);
    }
}
