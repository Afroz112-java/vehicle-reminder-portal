package net.konic.vehicle.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class LoginRequest {
    @NotNull(message="username is mandatory")
    private String username;
    @NotNull(message = "password is mandatory ")
    private String password;
}
