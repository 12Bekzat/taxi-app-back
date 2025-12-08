package com.taxi.app.repos;

// src/main/java/kz/redtaxi/order/OrderRepository.java
import com.taxi.app.models.Order;
import com.taxi.app.models.OrderStatus;
import com.taxi.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerAndStatusIn(User customer, List<OrderStatus> statuses);

    List<Order> findByDriverAndStatusIn(User driver, List<OrderStatus> statuses);

    // свободные заказы для водителей
    List<Order> findByStatus(OrderStatus status);

    // история для водителя
    List<Order> findByDriverAndStatusAndFinishedAtBetween(
            User driver,
            OrderStatus status,
            LocalDateTime from,
            LocalDateTime to
    );
}
