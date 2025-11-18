package net.konic.vehicle.service;

import lombok.RequiredArgsConstructor;
import net.konic.vehicle.entity.ReminderConfig;
import net.konic.vehicle.repository.ReminderConfigRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemainderConfigService {

    private final ReminderConfigRepository repository;

    public ReminderConfig getConfig() {
        return repository.findById(1L).orElseThrow(
                () -> new RuntimeException("ReminderConfig with ID=1 not found!")
        );
    }

    public ReminderConfig save(ReminderConfig config) {
        config.setId(1L); // Always update row 1
        return repository.save(config);
    }
}
