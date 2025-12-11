package com.taxi.app.dtos;

// WeatherService.java
public interface WeatherService {
    WeatherSnapshot getCurrentWeather(Double lat, Double lon);
}
