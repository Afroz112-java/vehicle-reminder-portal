package net.konic.vehicle.controller;

import net.konic.vehicle.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/profile")
    public ResponseEntity<Object> getUserProfile() {
        Long userId = 1L;
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
    @PutMapping("/profile")
    public ResponseEntity<Object> updateUserProfile(@RequestBody User updatedUser) {
        Long userId = 1L; // In real application, get this from authenticated user context
        User savedUser = userService.updateUserProfile(userId, updatedUser);
        return ResponseEntity.ok(savedUser);
    }



}
