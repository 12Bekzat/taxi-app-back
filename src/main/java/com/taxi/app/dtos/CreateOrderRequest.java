package com.taxi.app.dtos;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long equipmentTypeId;
    private String originAddress;
    private Double originLat;
    private Double originLon;
    private String destinationAddress;
    private Double destinationLat;
    private Double destinationLon;
    private Integer estimatedMinutes; // можно прислать 30 по умолчанию
}
