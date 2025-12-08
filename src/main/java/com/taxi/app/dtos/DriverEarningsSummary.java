package com.taxi.app.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DriverEarningsSummary {
    private long totalEarnings;
    private int totalOrders;
    private List<OrderResponse> orders;
}