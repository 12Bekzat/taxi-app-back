package com.taxi.app.services;

import com.taxi.app.dtos.ChatMessageDto;
import com.taxi.app.dtos.SendChatMessageRequest;
import com.taxi.app.models.ChatMessage;
import com.taxi.app.models.Order;
import com.taxi.app.models.User;
import com.taxi.app.repos.ChatMessageRepository;
import com.taxi.app.repos.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatRepo;
    private final OrderRepository orderRepository;

    private Order getAndCheckOrder(Long orderId, User current) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        boolean isCustomer = order.getCustomer() != null
                && order.getCustomer().getId().equals(current.getId());
        boolean isDriver = order.getDriver() != null
                && order.getDriver().getId().equals(current.getId());

        if (!isCustomer && !isDriver) {
            throw new RuntimeException("You are not participant of this order");
        }
        return order;
    }

    public List<ChatMessageDto> getMessages(Long orderId, Long lastId, User current) {
        Order order = getAndCheckOrder(orderId, current);

        List<ChatMessage> messages;
        if (lastId != null && lastId > 0) {
            messages = chatRepo.findByOrderAndIdGreaterThanOrderByIdAsc(order, lastId);
        } else {
            messages = chatRepo.findTop50ByOrderOrderByIdDesc(order);
            messages.sort(Comparator.comparing(ChatMessage::getId)); // по возрастанию
        }

        return messages.stream()
                .map(m -> toDto(m, current))
                .collect(toList());
    }

    public ChatMessageDto sendMessage(Long orderId, SendChatMessageRequest req, User current) {
        if (req.getText() == null || req.getText().trim().isEmpty()) {
            throw new RuntimeException("Text is empty");
        }

        Order order = getAndCheckOrder(orderId, current);

        User receiver;
        if (order.getCustomer().getId().equals(current.getId())) {
            receiver = order.getDriver();
        } else {
            receiver = order.getCustomer();
        }
        if (receiver == null) {
            throw new RuntimeException("Receiver is not assigned yet");
        }

        ChatMessage msg = ChatMessage.builder()
                .order(order)
                .sender(current)
                .receiver(receiver)
                .text(req.getText().trim())
                .createdAt(LocalDateTime.now())
                .build();

        chatRepo.save(msg);
        return toDto(msg, current);
    }

    private ChatMessageDto toDto(ChatMessage m, User current) {
        return ChatMessageDto.builder()
                .id(m.getId())
                .senderId(m.getSender().getId())
                .receiverId(m.getReceiver().getId())
                .text(m.getText())
                .createdAt(m.getCreatedAt())
                .fromMe(m.getSender().getId().equals(current.getId()))
                .build();
    }
}

