package com.example.vehicle.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")

public class VehicleEntity {

    @Id
    @GeneratedValue(stratergy = GenerationType.IDENTITY)
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
