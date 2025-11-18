package net.konic.vehicle.controller;

import lombok.RequiredArgsConstructor;
import net.konic.vehicle.entity.ReminderConfig;
import net.konic.vehicle.service.RemainderConfigService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reminder-config")
//@RequiredArgsConstructor
public class ReminderConfigController {

    private final RemainderConfigService service;

    public ReminderConfigController(RemainderConfigService service) {
        this.service = service;
    }

    @GetMapping
    public ReminderConfig getConfig() {
        return service.getConfig();
    }

    @PutMapping
    public ReminderConfig updateConfig(@RequestBody ReminderConfig config) {
        return service.save(config);
    }
}
