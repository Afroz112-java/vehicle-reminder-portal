package net.konic.vehicle.entity;

import jakarta.persistence.*;
import lombok.Data;


///**
// * This entity is used to store the reminder configuration in the database.
// * Why?
// *  - Because you want DYNAMIC SCHEDULING.
// *  - Instead of hardcoding the reminder time in @Scheduled,
// *    you store the values in the database, so you can change them anytime.
// *
// * Example:
// *  - serviceDaysBefore = 10  → Send email 10 days before service due date
// *  - insuranceDaysBefore = 20 → Send email 20 days before insurance expiry
// *  - cronExpression = "0 */5 * * * *" → Run scheduler every 5 minutes
//        *
//        * When your scheduler runs:
//        *  - It reads these values from the DB
// *  - So you do NOT need to restart the server when the timing changes
// */
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
