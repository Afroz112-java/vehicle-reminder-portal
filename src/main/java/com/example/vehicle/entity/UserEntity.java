package com.example.vehicle.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "users")

public class UserEntity {
    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private String email;
        private String password;
        private String phone;
        private String role; // instead of enum, just plain string for now
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

       // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        //private List<Vehicle> vehicles;
    }











