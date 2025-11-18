package net.konic.vehicle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity                            // Marks this class as a JPA entity (table)
@Table(name = "reminder_audit")    // Specifies the database table name
@Data                              // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor                 // Lombok: generates a no-argument constructor
@AllArgsConstructor                // Lombok: generates an all-arguments constructor
@Builder                           // Lombok: implements the builder pattern for this class
@EntityListeners(AuditingEntityListener.class)
public class ReminderAudit {

    @Id                             // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // Auto-generates the ID value
    private Long id;                                      // Primary key

    private Long userId;
    private String vehicleRegNumber;
    private String reminderType; // SERVICE / INSURANCE / BOTH
    private String email;
    private boolean emailSent;
    private String message;

    @CreatedDate                   // Auto-filled when the record is created
    private LocalDateTime sentAt;
}