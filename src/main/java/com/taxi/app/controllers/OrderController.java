package com.taxi.app.controllers;

import com.taxi.app.dtos.Address;
import com.taxi.app.dtos.CreateOrderRequest;
import com.taxi.app.dtos.DriverEarningsSummary;
import com.taxi.app.dtos.OrderResponse;
import com.taxi.app.models.User;
import com.taxi.app.services.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // клиент создаёт заказ
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal User user,
            @RequestBody CreateOrderRequest req
    ) {
        return ResponseEntity.ok(orderService.createOrder(req, user));
    }

    // активный заказ клиента
    @GetMapping("/me/active")
    public ResponseEntity<List<OrderResponse>> myActive(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(orderService.getCustomerActiveOrders(user));
    }

    // активный заказ водителя
    @GetMapping("/driver/active")
    public ResponseEntity<List<OrderResponse>> driverActive(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(orderService.getDriverActiveOrders(user));
    }

    // свободные заказы для водителей
    @GetMapping("/driver/available")
    public ResponseEntity<List<OrderResponse>> availableForDriver() {
        return ResponseEntity.ok(orderService.getAvailableOrdersForDrivers());
    }

    @PostMapping("/driver/{id}/accept")
    public ResponseEntity<OrderResponse> accept(
            @PathVariable Long id,
            @RequestBody Address destination,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(orderService.acceptOrder(id, user, destination));
    }

    @PostMapping("/driver/{id}/start")
    public ResponseEntity<OrderResponse> start(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(orderService.startOrder(id, user));
    }

    @PostMapping("/driver/{id}/finish")
    public ResponseEntity<OrderResponse> finish(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(orderService.finishOrder(id, user));
    }

    // доходы водителя за период
    @GetMapping("/driver/earnings")
    public ResponseEntity<DriverEarningsSummary> earnings(
            @AuthenticationPrincipal User user,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(orderService.getDriverEarnings(user, from, to));
    }

    @GetMapping("/customer/last-completed-unrated")
    public ResponseEntity<OrderResponse> customerLastCompletedUnrated(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(orderService.getCustomerLastOrder(user));
    }
}
