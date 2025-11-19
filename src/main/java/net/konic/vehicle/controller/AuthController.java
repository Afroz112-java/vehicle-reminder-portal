package net.konic.vehicle.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.konic.vehicle.dto.AuthResponse;
import net.konic.vehicle.dto.LoginRequest;
import net.konic.vehicle.dto.RegisterRequest;
import net.konic.vehicle.dto.RegisterResponse;
import net.konic.vehicle.security.JwtTokenUtil;
import net.konic.vehicle.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // Register new user (open endpoint)
    @PostMapping("/register")

    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        authService.register(request);  // no token created
        return ResponseEntity.ok(new RegisterResponse("Register successful"));
    }



//    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
//        AuthResponse response = authService.register(request);
//        return ResponseEntity.ok(response);
//
//    }




    // User login (generates JWT)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // Validate JWT token
    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam String token) {
        if (jwtTokenUtil.validateToken(token)) {
            String username = jwtTokenUtil.getUsernameFromToken(token); // Use correct method name
            return ResponseEntity.ok("Valid token for user: " + username);
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token!");
        }
    }
}
