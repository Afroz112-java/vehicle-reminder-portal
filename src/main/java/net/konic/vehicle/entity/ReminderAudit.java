package net.konic.vehicle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminder_audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ReminderAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String vehicleRegNumber;
    private String reminderType; // SERVICE / INSURANCE / BOTH
    private String email;
    private boolean emailSent;
    private String message;

    @CreatedDate
    private LocalDateTime sentAt;
}