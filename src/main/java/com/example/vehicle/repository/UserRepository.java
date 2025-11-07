package com.example.vehicle.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

    public interface UserRepository extends JpaRepository<VehicleEntity, Long> {
        Optional<VehicleEntity> findByEmail(String email);
        boolean existsByEmail(String email);
    }


