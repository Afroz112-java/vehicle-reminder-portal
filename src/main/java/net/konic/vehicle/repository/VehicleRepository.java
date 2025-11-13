package net.konic.vehicle.repository;

import net.konic.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// This is the Repository interface for Vehicle entity

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT v FROM Vehicle v WHERE v.serviceDueDate <= :targetDate")
    List<Vehicle> findVehiclesForServiceReminder(String targetDate);

    @Query("SELECT v FROM Vehicle v WHERE v.insuranceExpiryDate <= :targetDate")
    List<Vehicle> findVehiclesForInsuranceReminder(String targetDate);

    Optional<Vehicle> findByRegNumber(String regNumber);
}
