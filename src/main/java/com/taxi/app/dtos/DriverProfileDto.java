package com.taxi.app.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DriverProfileDto {
    private String licenseNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenseExpiry;

    private String vehicleType;
    private String vehicleBrandModel;
    private String vehicleColor;
    private String vehiclePlate;

    private String licenseFrontUrl;
    private String licenseBackUrl;
    private String idFrontUrl;
    private String idBackUrl;
    private String vehiclePhotoUrl;
}
