package com.taxi.app.models;

// src/main/java/kz/redtaxi/order/Order.java
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // кто создал
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    // какой водитель принял (может быть null до ACCEPTED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipment_type_id", nullable = false)
    private SpecialEquipmentType equipmentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.NEW;

    // маршрут
    private String originAddress;
    private Double originLat;
    private Double originLon;

    private String destinationAddress;
    private Double destinationLat;
    private Double destinationLon;

    // таймштампы
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime acceptedAt;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    // ценообразование
    @Column(nullable = false)
    private long pricePerMinute; // конечная цена за минуту в тынах

    @Column
    private Integer estimatedMinutes; // оценка до начала (например 30)

    @Column
    private Integer actualMinutes;

    @Column
    private Long totalPrice; // итоговая сумма в тынах

    // для отладки/аналитики
    private String pricingComment;
}
