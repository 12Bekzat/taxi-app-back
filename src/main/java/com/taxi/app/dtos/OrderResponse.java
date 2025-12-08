package com.taxi.app.dtos;
import com.taxi.app.models.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private OrderStatus status;

    private Long equipmentTypeId;
    private String equipmentName;

    private String originAddress;
    private String destinationAddress;

    private LocalDateTime createdAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private long pricePerMinute;
    private Integer estimatedMinutes;
    private Integer actualMinutes;
    private Long totalPrice;

    private String driverName;
    private String driverPhone;
}

