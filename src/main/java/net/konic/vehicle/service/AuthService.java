package net.konic.vehicle.service;


import net.konic.vehicle.dto.AuthResponse;
import net.konic.vehicle.dto.LoginRequest;
import net.konic.vehicle.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    boolean validateToken(String token);

    String getUsernameFromToken(String token);
}
