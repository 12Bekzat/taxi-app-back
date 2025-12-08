package com.taxi.app.services;

import com.taxi.app.dtos.*;
import com.taxi.app.models.CustomerProfile;
import com.taxi.app.models.DriverProfile;
import com.taxi.app.models.Role;
import com.taxi.app.models.User;
import com.taxi.app.repos.CustomerProfileRepository;
import com.taxi.app.repos.DriverProfileRepository;
import com.taxi.app.repos.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       DriverProfileRepository driverProfileRepository,
                       CustomerProfileRepository customerProfileRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.driverProfileRepository = driverProfileRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest req) {
        if (req.getPhone() == null || req.getPhone().isBlank()) {
            throw new RuntimeException("Phone is required");
        }

        if (userRepository.findByPhone(req.getPhone()).isPresent()) {
            throw new RuntimeException("User with this phone already exists");
        }

        Role role = req.getRole() != null ? req.getRole() : Role.CUSTOMER;

        User user = User.builder()
                .phone(req.getPhone())
                .email(req.getEmail()) // может быть null
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .avatarUrl(req.getAvatarUrl())
                .build();

        userRepository.save(user);

        if (role == Role.DRIVER) {
            DriverProfileDto dp = req.getDriverProfile();
            if (dp != null) {
                DriverProfile profile = DriverProfile.builder()
                        .user(user)
                        .licenseNumber(dp.getLicenseNumber())
                        .licenseExpiry(dp.getLicenseExpiry())
                        .vehicleType(dp.getVehicleType())
                        .vehicleBrandModel(dp.getVehicleBrandModel())
                        .vehicleColor(dp.getVehicleColor())
                        .vehiclePlate(dp.getVehiclePlate())
                        .licenseFrontUrl(dp.getLicenseFrontUrl())
                        .licenseBackUrl(dp.getLicenseBackUrl())
                        .idFrontUrl(dp.getIdFrontUrl())
                        .idBackUrl(dp.getIdBackUrl())
                        .vehiclePhotoUrl(dp.getVehiclePhotoUrl())
                        .build();
                driverProfileRepository.save(profile);
            }
        } else { // CUSTOMER
            CustomerProfileDto cp = req.getCustomerProfile();
            if (cp != null) {
                CustomerProfile profile = CustomerProfile.builder()
                        .user(user)
                        .defaultAddress(cp.getDefaultAddress())
                        .idFrontUrl(cp.getIdFrontUrl())
                        .idBackUrl(cp.getIdBackUrl())
                        .build();
                customerProfileRepository.save(profile);
            }
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest req) {
        User user = userRepository.findByPhone(req.getPhone())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public void updateMe(User user, UpdateProfileRequest req) {
        if (req.getFirstName() != null) user.setFirstName(req.getFirstName());
        if (req.getLastName() != null) user.setLastName(req.getLastName());
        if (req.getEmail() != null) user.setEmail(req.getEmail());

        userRepository.save(user); // добавь в контроллер UserRepository через конструктор
    }
}
