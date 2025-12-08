package com.taxi.app.dtos;

import lombok.Data;

@Data
public class DriverVehicleDto {
    private Long id;
    private Integer equipmentTypeId;
    private String equipmentName; // можешь подтягивать по справочнику
    private String model;
    private String plateNumber;
    private String color;
    private Integer year;
    private String photoUrl;
}
