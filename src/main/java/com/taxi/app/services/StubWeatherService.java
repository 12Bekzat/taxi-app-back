package com.taxi.app.services;

import com.taxi.app.dtos.WeatherCondition;
import com.taxi.app.dtos.WeatherService;
import com.taxi.app.dtos.WeatherSnapshot;
import org.springframework.stereotype.Service;

@Service
public class StubWeatherService implements WeatherService {

    @Override
    public WeatherSnapshot getCurrentWeather(Double lat, Double lon) {
        if (lat == null || lon == null) {
            // дефолт для Алматы
            return new WeatherSnapshot(WeatherCondition.CLEAR, 20.0);
        }

        if (lat > 50) {
            // условно "севернее" – пусть снег
            return new WeatherSnapshot(WeatherCondition.SNOW, -5.0);
        }

        // иначе просто ясно и тепло
        return new WeatherSnapshot(WeatherCondition.CLEAR, 25.0);
    }
}
