package net.konic.vehicle.controller;


import lombok.RequiredArgsConstructor;
import net.konic.vehicle.dto.AuthResponse;
import net.konic.vehicle.dto.LoginRequest;
import net.konic.vehicle.dto.RegisterRequest;
import net.konic.vehicle.security.JwtTokenUtil;
import net.konic.vehicle.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;

    // Register new user (open endpoint)
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // User login (generates JWT)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // Validate JWT token
    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam String token) {
        if (jwtTokenUtil.validateToken(token)) {
            String username = jwtTokenUtil.getUsername(token); // Use correct method name
            return ResponseEntity.ok("Valid token for user: " + username);
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token!");
        }
    }
}
