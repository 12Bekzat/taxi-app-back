package com.taxi.app.dtos;

// WeatherSnapshot.java
public class WeatherSnapshot {
    private final WeatherCondition condition;
    private final double temperatureC;

    public WeatherSnapshot(WeatherCondition condition, double temperatureC) {
        this.condition = condition;
        this.temperatureC = temperatureC;
    }

    public WeatherCondition getCondition() {
        return condition;
    }

    public double getTemperatureC() {
        return temperatureC;
    }
}
