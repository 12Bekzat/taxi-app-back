package com.taxi.app.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "driver_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // связь 1:1 с User
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // паспортные / ФИО можно держать в User, тут только профиль по технике/правам

    // Права
    private String licenseNumber;          // серия и номер прав
    private LocalDate licenseExpiry;       // срок действия прав

    // Техника
    private String vehicleType;            // эвакуатор / манипулятор / ...
    private String vehicleBrandModel;      // например MAN TGL
    private String vehicleColor;
    private String vehiclePlate;           // госномер

    // Фото-документы (URL / путь к файлу)
    private String licenseFrontUrl;
    private String licenseBackUrl;

    private String idFrontUrl;
    private String idBackUrl;

    private String vehiclePhotoUrl;        // фотка техники
}
