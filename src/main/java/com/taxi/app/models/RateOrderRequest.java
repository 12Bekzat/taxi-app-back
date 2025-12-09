package com.taxi.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateOrderRequest {
    private int score;       // 1..5
    private String comment;  // optional
}
