package com.taxi.app.repos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DriverRatingDto {
    private Long id;
    private Long orderId;
    private int score;
    private String comment;
    private LocalDateTime createdAt;

    private Long customerId;
    private String customerName;
}
