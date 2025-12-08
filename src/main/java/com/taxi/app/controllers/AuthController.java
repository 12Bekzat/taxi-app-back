package com.taxi.app.controllers;

// src/main/java/kz/redtaxi/auth/AuthController.java
import com.taxi.app.dtos.AuthRequest;
import com.taxi.app.dtos.AuthResponse;
import com.taxi.app.dtos.RegisterRequest;
import com.taxi.app.dtos.UpdateProfileRequest;
import com.taxi.app.models.DriverDocument;
import com.taxi.app.models.DriverVehicle;
import com.taxi.app.models.User;
import com.taxi.app.repos.DriverDocumentRepository;
import com.taxi.app.repos.DriverVehicleRepository;
import com.taxi.app.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // потом можно сузить
public class AuthController {

    private final AuthService authService;
    @Autowired
    private DriverVehicleRepository driverVehicleRepository;
    @Autowired
    private DriverDocumentRepository driverDocumentRepository;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal User user) {
        if (user == null) return ResponseEntity.status(401).build();

        List<DriverDocument> byDriver = driverDocumentRepository.findByDriver(user);

        return ResponseEntity.ok(
                new Object() {
                    public final Long id = user.getId();
                    public final String phone = user.getPhone();
                    public final String email = user.getEmail(); // может быть null
                    public final String role = user.getRole().name();
                    public final String firstName = user.getFirstName();
                    public final String lastName = user.getLastName();
                    public final String avatarUrl = user.getAvatarUrl();
                    public final boolean driverDocsCompleted = !byDriver.isEmpty();
                }
        );
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateProfileRequest req
    ) {
        if (user == null) return ResponseEntity.status(401).build();

        authService.updateMe(user, req);

        return ResponseEntity.ok(
                new Object() {
                    public final Long id = user.getId();
                    public final String phone = user.getPhone();
                    public final String email = user.getEmail();
                    public final String role = user.getRole().name();
                    public final String firstName = user.getFirstName();
                    public final String lastName = user.getLastName();
                    public final String avatarUrl = user.getAvatarUrl();
                }
        );
    }
}

