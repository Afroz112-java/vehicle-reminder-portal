package net.konic.vehicle.repository;

import net.konic.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    interface UserRepository<VehicleEntity> extends JpaRepository<VehicleEntity, Long> {
        Optional<VehicleEntity> findByEmail(String email);
        boolean existsByEmail(String email);
    }
}
