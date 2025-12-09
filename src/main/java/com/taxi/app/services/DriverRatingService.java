package com.taxi.app.services;

import com.taxi.app.models.*;
import com.taxi.app.repos.DriverRatingDto;
import com.taxi.app.repos.DriverRatingRepository;
import com.taxi.app.repos.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class DriverRatingService {

    private final OrderRepository orderRepository;
    private final DriverRatingRepository ratingRepository;

    /**
     * Клиент оценивает водителя по заказу.
     */
    public DriverRatingDto rateOrder(Long orderId, User customer, RateOrderRequest req) {
        if (req.getScore() < 1 || req.getScore() > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getCustomer() == null ||
                !order.getCustomer().getId().equals(customer.getId())) {
            throw new RuntimeException("Order does not belong to this customer");
        }

        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new RuntimeException("Order is not completed");
        }

        if (order.getDriver() == null) {
            throw new RuntimeException("Order has no driver");
        }

        ratingRepository.findByOrder(order).ifPresent(r -> {
            throw new RuntimeException("Order already rated");
        });

        DriverRating rating = DriverRating.builder()
                .customer(customer)
                .driver(order.getDriver())
                .order(order)
                .score(req.getScore())
                .comment(req.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        ratingRepository.save(rating);

        return toDto(rating);
    }

    /**
     * Сводка по рейтингу водителя + список отзывов.
     */
    public DriverRatingSummaryDto getDriverRatingSummary(User driver) {
        List<DriverRating> ratings = ratingRepository.findByDriverOrderByCreatedAtDesc(driver);

        long count = ratings.size();
        double avg = 0.0;
        if (count > 0) {
            avg = ratings.stream()
                    .mapToInt(DriverRating::getScore)
                    .average()
                    .orElse(0.0);
        }

        List<DriverRatingDto> ratingDtos = ratings.stream()
                .map(this::toDto)
                .collect(toList());

        return DriverRatingSummaryDto.builder()
                .averageScore(avg)
                .ratingsCount(count)
                .ratings(ratingDtos)
                .build();
    }

    public boolean isExistingDriverRating(Order order) {
        if (order == null) { return false; }
        Optional<DriverRating> byOrder = ratingRepository.findByOrder(order);
        return byOrder.isPresent();
    }

    private DriverRatingDto toDto(DriverRating r) {
        User c = r.getCustomer();
        String name = ((c.getFirstName() != null ? c.getFirstName() : "") + " " +
                (c.getLastName() != null ? c.getLastName() : "")).trim();
        if (name.isEmpty()) {
            name = "Клиент";
        }

        return DriverRatingDto.builder()
                .id(r.getId())
                .orderId(r.getOrder().getId())
                .score(r.getScore())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .customerId(c.getId())
                .customerName(name)
                .build();
    }
}
