package net.konic.vehicle.repository;

import net.konic.vehicle.entity.ReminderConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderConfigRepository extends JpaRepository<ReminderConfig, Long> {
}
