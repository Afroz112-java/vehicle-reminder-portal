package net.konic.vehicle.repository;

import net.konic.vehicle.entity.ReminderConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for ReminderConfig entity.
 *
 * Why do you need this?
 * ---------------------
 * 1. To communicate with the database.
 *    - This repository automatically gives methods to fetch, insert,
 *      update, and delete ReminderConfig (without writing SQL).
 *
 * 2. Used by ReminderConfigService to get the current reminder settings.
 *
 * 3. JPA provides these functions automatically:
 *      - findById()
 *      - findAll()
 *      - save()
 *      - deleteById()
 *
 * 4. Scheduler uses the configuration fetched from this repository.
 *
 * Example:
 *  ReminderConfig config = repository.findById(1L).orElseThrow();
 *  String cron = config.getCronExpression();  // dynamic cron expression
 *
 * Spring Data JPA removes the need to manually write queries for basic operations.
 */


public interface ReminderConfigRepository extends JpaRepository<ReminderConfig, Long> {
}
