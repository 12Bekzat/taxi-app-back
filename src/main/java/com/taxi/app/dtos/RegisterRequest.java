package com.taxi.app.dtos;

import com.taxi.app.models.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String phone;      // ОБЯЗАТЕЛЬНО
    private String email;      // опционально
    private String password;
    private Role role; // CUSTOMER или DRIVER

    // общее
    private String firstName;
    private String lastName;
    private String avatarUrl;

    // специфичные профили
    private DriverProfileDto driverProfile;
    private CustomerProfileDto customerProfile;
}

