package net.konic.vehicle.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull(message = "username is mandatory")
    private String username;

    @NotNull(message = "password is mandatory")
    private String password;

    @NotNull(message = "role is mandatory")
    private String role = "USER";

    @NotNull(message = "phonenum is mandatory")
    private  String phonenum;

    @NotNull(message = "email is mandatory")
    private String email;// default role
}


