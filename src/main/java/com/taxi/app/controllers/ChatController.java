package com.taxi.app.controllers;

import com.taxi.app.dtos.ChatMessageDto;
import com.taxi.app.dtos.SendChatMessageRequest;
import com.taxi.app.models.User;
import com.taxi.app.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * Получить сообщения по заказу.
     * query param lastId - опционально, для получения только новых сообщений.
     */
    @GetMapping("/orders/{orderId}")
    public List<ChatMessageDto> getMessages(
            @PathVariable Long orderId,
            @RequestParam(required = false) Long lastId,
            @AuthenticationPrincipal User current
    ) {
        return chatService.getMessages(orderId, lastId, current);
    }

    /**
     * Отправить сообщение по заказу.
     */
    @PostMapping("/orders/{orderId}")
    public ChatMessageDto sendMessage(
            @PathVariable Long orderId,
            @RequestBody SendChatMessageRequest request,
            @AuthenticationPrincipal User current
    ) {
        return chatService.sendMessage(orderId, request, current);
    }
}

