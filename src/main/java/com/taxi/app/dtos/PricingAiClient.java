package com.taxi.app.dtos;

// PricingAiClient.java
public interface PricingAiClient {
    double getAiMultiplier(String equipmentCode,
                           long basePricePerMinute,
                           double demandIndex,
                           WeatherCondition condition);
}
