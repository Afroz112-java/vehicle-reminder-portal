package net.konic.vehicle.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.konic.vehicle.dto.Userdto;
import net.konic.vehicle.dto.UpdateUserdto;
import net.konic.vehicle.dto.Userdto;
import net.konic.vehicle.entity.UserEntity;
import net.konic.vehicle.execption.ResourceNotFoundException;
import net.konic.vehicle.repository.UserRepository;
import net.konic.vehicle.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public Userdto getProfile(Long userId) {
        log.info("Fetching profile for userId={}", userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return mapTodto(user);
    }


    @Transactional
    public Userdto updateProfile(Long userId, UpdateUserdto dto) {
        log.info("Updating profile for userId={}", userId);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());

        user.setUpdatedAt(LocalDateTime.now());

        UserEntity updated = userRepository.save(user);
        log.info("User [{}] profile updated successfully.", updated.getEmail());

        return mapTodto(updated);
    }

    private Userdto mapTodto(UserEntity user) {
        Userdto dto = new Userdto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public User getUserById(Long userId) {
        return getUserById(userId);
    }

    public User updateUserProfile(Long userId, User updatedUser) {
        return updatedUser;
    }
}