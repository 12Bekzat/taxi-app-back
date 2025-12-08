package com.taxi.app.models;

import lombok.Data;

@Data
public class RateOrderRequest {
    private int score;       // 1..5
    private String comment;  // optional
}
