package com.taxi.app.services;

import com.taxi.app.dtos.*;
import com.taxi.app.models.SpecialEquipmentType;
import com.taxi.app.repos.SpecialEquipmentTypeRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentPricingService {

    private final SpecialEquipmentTypeRepository equipmentRepository;
    private final WeatherService weatherService;
    private final DemandService demandService;
    private final PricingAiClient pricingAiClient;

    public MinutePriceResponse getPricePerMinute(
            String equipmentCode,
            Long regionId,
            Double lat,
            Double lon
    ) {
        Optional<SpecialEquipmentType> first = equipmentRepository
                .findAll().stream().filter(x -> x.getCode().equals(equipmentCode)).findFirst();

        if (first.isEmpty()) {
            throw new IllegalArgumentException("Unknown equipmentCode: " + equipmentCode);
        }
        SpecialEquipmentType type = first.get();

        long basePricePerMinute = type.getBasePricePerMinute();

        WeatherSnapshot weather = weatherService.getCurrentWeather(lat, lon);
        double weatherCoef = calculateWeatherCoef(weather, equipmentCode);

        DemandSnapshot demand = demandService.getCurrentDemand(regionId, equipmentCode);
        double demandCoef = calculateDemandCoef(demand);

        double aiCoef = pricingAiClient.getAiMultiplier(
                equipmentCode,
                basePricePerMinute,
                demand.getDemandIndex(),
                weather.getCondition()
        );
        if (aiCoef <= 0) {
            aiCoef = 1.0;
        }

        double finalPrice = basePricePerMinute
                * weatherCoef
                * demandCoef
                * aiCoef;

        int finalPriceRounded = (int) Math.round(finalPrice);

        return new MinutePriceResponse(
                equipmentCode,
                basePricePerMinute,
                weatherCoef,
                demandCoef,
                aiCoef,
                finalPriceRounded,
                "KZT"
        );
    }

    private double calculateWeatherCoef(WeatherSnapshot weather, String equipmentCode) {
        return switch (weather.getCondition()) {
            case STORM -> 1.3;
            case SNOW -> 1.2;
            case RAIN -> 1.1;
            case FOG -> 1.05;
            case CLEAR -> 1.0;
        };
    }

    private double calculateDemandCoef(DemandSnapshot demand) {
        double idx = demand.getDemandIndex();

        if (idx < 3) return 0.9;
        if (idx < 7) return 1.0;
        if (idx < 9) return 1.2;
        return 1.5;
    }
}
