package com.example.vehicle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String regNumber;
    private String brand;
    private String model;
    private LocalDate insuranceExpiryDate;
    private LocalDate serviceDueDate;
    private String User;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active = true;
}
