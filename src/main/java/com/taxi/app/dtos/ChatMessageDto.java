package com.taxi.app.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageDto {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String text;
    private LocalDateTime createdAt;

    // удобный флаг для фронта
    private boolean fromMe;
}

