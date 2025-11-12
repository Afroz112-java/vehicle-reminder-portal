package net.konic.vehicle.repository;

import net.konic.vehicle.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    // Find a user by username
    Optional<AuthEntity> findByUsername(String username);

    // Check if a username already exists
    boolean existsByUsername(String username);
}
