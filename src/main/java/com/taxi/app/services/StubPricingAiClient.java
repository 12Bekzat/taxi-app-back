package com.taxi.app.services;

import com.taxi.app.dtos.PricingAiClient;
import com.taxi.app.dtos.WeatherCondition;
import org.springframework.stereotype.Service;

@Service
public class StubPricingAiClient implements PricingAiClient {

    @Override
    public double getAiMultiplier(String equipmentCode,
                                  long basePricePerMinute,
                                  double demandIndex,
                                  WeatherCondition condition) {
        if (equipmentCode == null) {
            return 1.0;
        }

        String code = equipmentCode.toUpperCase();

        // имитируем "умный" ИИ, который чуть дороже ставит тяжелую технику
        return switch (code) {
            case "TRUCK" -> 1.10;
            case "MANIP" -> 1.05;
            default -> 1.0; // EVAC и всё остальное – без надбавки
        };
    }
}

