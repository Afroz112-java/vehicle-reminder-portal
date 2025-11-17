package net.konic.vehicle.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ReminderConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // How many days before service due date
    private int serviceDaysBefore;

    // How many days before insurance expiry
    private int insuranceDaysBefore;

    // The cron expression for dynamic scheduling
    private String cronExpression;
}
