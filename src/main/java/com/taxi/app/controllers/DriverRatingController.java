package com.taxi.app.controllers;

import com.taxi.app.models.DriverRatingSummaryDto;
import com.taxi.app.models.RateOrderRequest;
import com.taxi.app.models.User;
import com.taxi.app.repos.DriverRatingDto;
import com.taxi.app.services.DriverRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class DriverRatingController {

    private final DriverRatingService ratingService;

    /**
     * Клиент ставит оценку по заказу.
     */
    @PostMapping("/orders/{orderId}")
    public DriverRatingDto rateOrder(
            @PathVariable Long orderId,
            @RequestBody RateOrderRequest request,
            @AuthenticationPrincipal User customer
    ) {
        return ratingService.rateOrder(orderId, customer, request);
    }

    /**
     * Водитель смотрит свой рейтинг и отзывы.
     */
    @GetMapping("/driver/me")
    public DriverRatingSummaryDto getMyRating(@AuthenticationPrincipal User driver) {
        return ratingService.getDriverRatingSummary(driver);
    }
}
