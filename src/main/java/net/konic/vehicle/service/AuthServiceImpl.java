package net.konic.vehicle.service;


import lombok.RequiredArgsConstructor;
import net.konic.vehicle.dto.AuthResponse;
import net.konic.vehicle.dto.LoginRequest;
import net.konic.vehicle.dto.RegisterRequest;
import net.konic.vehicle.entity.AuthEntity;
import net.konic.vehicle.repository.AuthRepository;
import net.konic.vehicle.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  JwtTokenUtil jwtTokenUtil;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Check if user exists
        if (authRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }

        // Create new user entity
        AuthEntity user = new AuthEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : "USER");
        user.setEmail(request.getEmail());
        user.setPhonenum(request.getPhonenum());

        authRepository.save(user);

//        // Generate JWT token
//        String token = jwtTokenUtil.generateToken(user.getUsername());
//
//        return new AuthResponse(token, "User registered successfully");
        return new AuthResponse(null, "Register successful");

    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // Fetch user by username
        AuthEntity user = authRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException( "Invalid credentials");
        }

        // Generate JWT token
        String token = jwtTokenUtil.generateToken(user.getUsername());

        return new AuthResponse(token, "Login successful");
    }

    @Override
    public boolean validateToken(String token) {
        return false;
    }

    @Override
    public String getUsernameFromToken(String token) {
        return "";
    }
}
