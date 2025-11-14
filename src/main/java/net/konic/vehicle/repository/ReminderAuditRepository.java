package net.konic.vehicle.repository;

import net.konic.vehicle.entity.ReminderAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderAuditRepository extends JpaRepository<ReminderAudit, Long> {


}