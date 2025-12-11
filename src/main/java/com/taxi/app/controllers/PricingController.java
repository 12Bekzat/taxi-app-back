package com.taxi.app.controllers;

import com.taxi.app.dtos.MinutePriceResponse;
import com.taxi.app.services.EquipmentPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final EquipmentPricingService pricingService;

    @GetMapping("/per-minute")
    public MinutePriceResponse getEquipmentPricePerMinute(
            @RequestParam String equipmentCode,   // EVAC / MANIP / TRUCK
            @RequestParam Long regionId,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon
    ) {
        return pricingService.getPricePerMinute(equipmentCode, regionId, lat, lon);
    }
}

