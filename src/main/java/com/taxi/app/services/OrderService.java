package com.taxi.app.services;

import com.taxi.app.dtos.Address;
import com.taxi.app.dtos.CreateOrderRequest;
import com.taxi.app.dtos.DriverEarningsSummary;
import com.taxi.app.dtos.OrderResponse;
import com.taxi.app.models.*;
import com.taxi.app.repos.DriverRatingRepository;
import com.taxi.app.repos.OrderRepository;
import com.taxi.app.repos.SpecialEquipmentTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final SpecialEquipmentTypeRepository equipmentRepository;
    private final DynamicPricingService pricingService;
    private final DriverRatingService driverRatingService;

    public OrderService(OrderRepository orderRepository,
                        SpecialEquipmentTypeRepository equipmentRepository,
                        DynamicPricingService pricingService, DriverRatingService driverRatingService) {
        this.orderRepository = orderRepository;
        this.equipmentRepository = equipmentRepository;
        this.pricingService = pricingService;
        this.driverRatingService = driverRatingService;
    }

    public OrderResponse createOrder(CreateOrderRequest req, User customer) {
        SpecialEquipmentType type = equipmentRepository.findById(req.getEquipmentTypeId())
                .orElseThrow(() -> new RuntimeException("Equipment type not found"));

        long pricePerMinute = pricingService.calculatePricePerMinute(type);
        int estimated = req.getEstimatedMinutes() != null ? req.getEstimatedMinutes() : type.getDefaultMinutes();

        Order order = Order.builder()
                .customer(customer)
                .equipmentType(type)
                .status(OrderStatus.NEW)
                .originAddress(req.getOriginAddress())
                .originLat(req.getOriginLat())
                .originLon(req.getOriginLon())
                .destinationAddress(req.getDestinationAddress())
                .destinationLat(req.getDestinationLat())
                .destinationLon(req.getDestinationLon())
                .createdAt(LocalDateTime.now())
                .pricePerMinute(pricePerMinute)
                .estimatedMinutes(estimated)
                .pricingComment("dynamic pricing v1")
                .build();

        orderRepository.save(order);
        return toResponse(order);
    }

    public List<OrderResponse> getCustomerActiveOrders(User customer) {
        List<Order> orders = orderRepository.findByCustomerAndStatusIn(
                customer,
                List.of(OrderStatus.NEW, OrderStatus.ACCEPTED, OrderStatus.IN_PROGRESS)
        );
        return orders.stream().map(this::toResponse).collect(toList());
    }

    public OrderResponse getCustomerLastOrder(User customer) {
        List<Order> orders = orderRepository.findByCustomerAndStatusIn(
                customer,
                List.of(OrderStatus.COMPLETED)
        );
        orders.sort(Comparator.comparing(Order::getCreatedAt).reversed());

        if(driverRatingService.isExistingDriverRating(orders.get(0))) return null;

        return toResponse(orders.get(0));
    }

    public List<OrderResponse> getDriverActiveOrders(User driver) {
        List<Order> orders = orderRepository.findByDriverAndStatusIn(
                driver,
                List.of(OrderStatus.ACCEPTED, OrderStatus.IN_PROGRESS)
        );
        return orders.stream().map(this::toResponse).collect(toList());
    }

    public List<OrderResponse> getAvailableOrdersForDrivers() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.NEW);
        return orders.stream().map(this::toResponse).collect(toList());
    }

    public OrderResponse acceptOrder(Long orderId, User driver, Address destination) {
        if (driver.getRole() != Role.DRIVER) {
            throw new RuntimeException("Only driver can accept orders");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.NEW) {
            throw new RuntimeException("Order already taken");
        }

        order.setDriver(driver);
        order.setStatus(OrderStatus.ACCEPTED);
        order.setAcceptedAt(LocalDateTime.now());
        order.setDestinationLat(destination.getLatitude());
        order.setDestinationLon(destination.getLongitude());
        return toResponse(order);
    }

    public OrderResponse startOrder(Long orderId, User driver) {
        Order order = getDriverOrder(orderId, driver);

        if (order.getStatus() != OrderStatus.ACCEPTED) {
            throw new RuntimeException("Cannot start, wrong status");
        }
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setStartedAt(LocalDateTime.now());
        return toResponse(order);
    }

    public OrderResponse finishOrder(Long orderId, User driver) {
        Order order = getDriverOrder(orderId, driver);

        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new RuntimeException("Cannot finish, wrong status");
        }
        LocalDateTime finished = LocalDateTime.now();
        order.setFinishedAt(finished);
        order.setStatus(OrderStatus.COMPLETED);

        int minutes = (int) Math.max(
                1,
                Math.ceil(
                        Duration.between(order.getStartedAt(), finished).getSeconds() / 60.0
                )
        );
        order.setActualMinutes(minutes);
        long total = minutes * order.getPricePerMinute();
        order.setTotalPrice(total);

        return toResponse(order);
    }

    public DriverEarningsSummary getDriverEarnings(User driver, LocalDateTime from, LocalDateTime to) {
        List<Order> orders = orderRepository.findByDriverAndStatusAndFinishedAtBetween(
                driver,
                OrderStatus.COMPLETED,
                from,
                to
        );

        long total = orders.stream()
                .mapToLong(o -> o.getTotalPrice() != null ? o.getTotalPrice() : 0L)
                .sum();

        List<OrderResponse> responses = orders.stream()
                .map(this::toResponse)
                .collect(toList());

        return DriverEarningsSummary.builder()
                .totalEarnings(total)
                .totalOrders(orders.size())
                .orders(responses)
                .build();
    }

    private Order getDriverOrder(Long id, User driver) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getDriver() == null || !order.getDriver().getId().equals(driver.getId())) {
            throw new RuntimeException("Order belongs to another driver");
        }
        return order;
    }

    private OrderResponse toResponse(Order o) {
        return OrderResponse.builder()
                .id(o.getId())
                .status(o.getStatus())
                .equipmentTypeId(o.getEquipmentType().getId())
                .equipmentName(o.getEquipmentType().getName())
                .originAddress(o.getOriginAddress())
                .destinationAddress(o.getDestinationAddress())
                .createdAt(o.getCreatedAt())
                .acceptedAt(o.getAcceptedAt())
                .originLat(o.getOriginLat())
                .originLon(o.getOriginLon())
                .destinationLat(o.getDestinationLat())
                .destinationLon(o.getDestinationLon())
                .startedAt(o.getStartedAt())
                .finishedAt(o.getFinishedAt())
                .pricePerMinute(o.getPricePerMinute())
                .estimatedMinutes(o.getEstimatedMinutes())
                .actualMinutes(o.getActualMinutes())
                .totalPrice(o.getTotalPrice())
                .driverName(o.getDriver() != null
                        ? (o.getDriver().getFirstName() + " " + o.getDriver().getLastName()).trim()
                        : null)
                .driverPhone(o.getDriver() != null ? o.getDriver().getPhone() : null)
                .build();
    }
}
