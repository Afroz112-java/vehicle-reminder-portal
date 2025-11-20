package net.konic.vehicle.repository;

import net.konic.vehicle.entity.ReminderAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReminderAuditRepository extends JpaRepository<ReminderAudit, Long> {

    // clean JPA method (correct)
    boolean existsByVehicleRegNumberAndReminderTypeAndSentAtAfter(
            String vehicleRegNumber,
            String reminderType,
            LocalDateTime sentAt
    );

    ReminderAudit findFirstByVehicleIdOrderBySentAtDesc(Long id);
    Optional<ReminderAudit> findTopByVehicleRegNumberOrderBySentAtDesc(String regNumber);



}


