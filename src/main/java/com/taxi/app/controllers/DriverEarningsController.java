package com.taxi.app.controllers;

import com.taxi.app.dtos.DriverEarningsSummary;
import com.taxi.app.models.Order;
import com.taxi.app.models.OrderStatus;
import com.taxi.app.models.User;
import com.taxi.app.repos.OrderRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;

@RestController
@RequestMapping("/api/driver/earnings")
public class DriverEarningsController {

    private final OrderRepository orderRepository;

    public DriverEarningsController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}

