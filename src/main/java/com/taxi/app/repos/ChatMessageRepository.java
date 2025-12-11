package com.taxi.app.repos;

import com.taxi.app.models.ChatMessage;
import com.taxi.app.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // первые сообщения (последние 50)
    List<ChatMessage> findTop50ByOrderOrderByIdDesc(Order order);

    // новые сообщения после lastId
    List<ChatMessage> findByOrderAndIdGreaterThanOrderByIdAsc(Order order, Long lastId);
}
