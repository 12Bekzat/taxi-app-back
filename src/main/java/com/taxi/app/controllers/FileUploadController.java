package com.taxi.app.controllers;


import com.taxi.app.models.CustomerProfile;
import com.taxi.app.models.DriverProfile;
import com.taxi.app.models.Role;
import com.taxi.app.models.User;
import com.taxi.app.repos.CustomerProfileRepository;
import com.taxi.app.repos.DriverProfileRepository;
import com.taxi.app.repos.UserRepository;
import com.taxi.app.services.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileUploadController {

    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final CustomerProfileRepository customerProfileRepository;

    public FileUploadController(FileStorageService fileStorageService,
                                UserRepository userRepository,
                                DriverProfileRepository driverProfileRepository,
                                CustomerProfileRepository customerProfileRepository) {
        this.fileStorageService = fileStorageService;
        this.userRepository = userRepository;
        this.driverProfileRepository = driverProfileRepository;
        this.customerProfileRepository = customerProfileRepository;
    }

    // ---------- АВАТАР ----------

    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String url = fileStorageService.store(file, "avatars", "avatar-" + user.getId());
        user.setAvatarUrl(url);
        userRepository.save(user);

        return ResponseEntity.ok(new SimpleUrlResponse(url));
    }

    // ---------- ВОДИТЕЛЬ ----------

    // field: license-front | license-back | id-front | id-back | vehicle
    @PostMapping("/driver/{field}")
    public ResponseEntity<?> uploadDriverDoc(
            @AuthenticationPrincipal User user,
            @PathVariable String field,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.getRole() != Role.DRIVER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only drivers can upload driver docs");
        }

        DriverProfile profile = driverProfileRepository.findByUser(user)
                .orElseGet(() -> {
                    DriverProfile p = new DriverProfile();
                    p.setUser(user);
                    return p;
                });

        String subDir = "driver/" + user.getId();
        String prefix = "drv-" + user.getId() + "-" + field;
        String url = fileStorageService.store(file, subDir, prefix);

        switch (field.toLowerCase()) {
            case "license-front" -> profile.setLicenseFrontUrl(url);
            case "license-back" -> profile.setLicenseBackUrl(url);
            case "id-front" -> profile.setIdFrontUrl(url);
            case "id-back" -> profile.setIdBackUrl(url);
            case "vehicle" -> profile.setVehiclePhotoUrl(url);
            default -> {
                return ResponseEntity.badRequest().body("Unknown field: " + field);
            }
        }

        driverProfileRepository.save(profile);
        return ResponseEntity.ok(new SimpleUrlResponse(url));
    }

    // ---------- КЛИЕНТ ----------

    // field: id-front | id-back
    @PostMapping("/customer/{field}")
    public ResponseEntity<?> uploadCustomerDoc(
            @AuthenticationPrincipal User user,
            @PathVariable String field,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.getRole() != Role.CUSTOMER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only customers can upload customer docs");
        }

        CustomerProfile profile = customerProfileRepository.findByUser(user)
                .orElseGet(() -> {
                    CustomerProfile p = new CustomerProfile();
                    p.setUser(user);
                    return p;
                });

        String subDir = "customer/" + user.getId();
        String prefix = "cust-" + user.getId() + "-" + field;
        String url = fileStorageService.store(file, subDir, prefix);

        switch (field.toLowerCase()) {
            case "id-front" -> profile.setIdFrontUrl(url);
            case "id-back" -> profile.setIdBackUrl(url);
            default -> {
                return ResponseEntity.badRequest().body("Unknown field: " + field);
            }
        }

        customerProfileRepository.save(profile);
        return ResponseEntity.ok(new SimpleUrlResponse(url));
    }

    // простой DTO для ответа
    public record SimpleUrlResponse(String url) {}
}
