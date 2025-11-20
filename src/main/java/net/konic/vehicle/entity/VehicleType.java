package net.konic.vehicle.entity;

/**
 * Enum representing the type of vehicle.
 * This is stored in the database using @Enumerated(EnumType.STRING)
 * inside the Vehicle entity.

 * Allowed values:
 *   - CAR
 *   - BIKE
 */
public enum VehicleType {
    CAR,
    BIKE
}
