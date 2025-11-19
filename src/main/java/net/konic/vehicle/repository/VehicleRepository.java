package net.konic.vehicle.repository;

import net.konic.vehicle.entity.Vehicle;
import net.konic.vehicle.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// This is the Repository interface for Vehicle entity

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT v FROM Vehicle v WHERE v.serviceDueDate <= :targetDate AND v.serviceReminderSent = false")
    List<Vehicle> findVehiclesForServiceReminder(@Param("targetDate") LocalDate targetDate);

    @Query("SELECT v FROM Vehicle v WHERE v.insuranceExpiryDate <= :targetDate AND v.insuranceReminderSent = false")
    List<Vehicle> findVehiclesForInsuranceReminder(@Param("targetDate") LocalDate targetDate);

    Optional<Vehicle> findByRegNumber(String regNumber);

    // ⭐ Filter by vehicle type (CAR/BIKE)
    List<Vehicle> findByVehicleType(VehicleType vehicleType);

    // ⭐ Filter by type AND user
    List<Vehicle> findByUserIdAndVehicleType(Long userId, VehicleType vehicleType);
}
