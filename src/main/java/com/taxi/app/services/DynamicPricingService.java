package com.taxi.app.services;

// src/main/java/kz/redtaxi/pricing/DynamicPricingService.java
import com.taxi.app.models.SpecialEquipmentType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DynamicPricingService {

    /**
     * Рассчитывает цену за минуту с учётом:
     * - часа дня (час-пик)
     * - дня недели (выходные)
     * - "плохой погоды" (заглушка)
     */
    public long calculatePricePerMinute(SpecialEquipmentType type) {
        LocalDateTime now = LocalDateTime.now();

        double multiplier = 1.0;

        int hour = now.getHour();
        // 8-11 и 17-20 — 30% дороже
        if ((hour >= 8 && hour <= 11) || (hour >= 17 && hour <= 20)) {
            multiplier += 0.3;
        }

        int dow = now.getDayOfWeek().getValue(); // 1..7
        if (dow == 6 || dow == 7) {
            multiplier += 0.15; // выходные чуть дороже
        }

        // просто пример "плохой погоды": с 0 до 3 часа
        if (hour >= 0 && hour <= 3) {
            multiplier += 0.2;
        }

        long result = Math.round(type.getBasePricePerMinute() * multiplier);
        return Math.max(result, type.getBasePricePerMinute()); // не ниже базы
    }
}
