package net.konic.vehicle.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role = "USER";
    private  String phonenum;
    private String email;// default role
}


