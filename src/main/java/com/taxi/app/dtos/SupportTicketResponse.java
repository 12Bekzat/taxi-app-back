package com.taxi.app.dtos;


import com.taxi.app.models.SupportStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class SupportTicketResponse {
    private Long id;
    private String subject;
    private String message;
    private SupportStatus status;
    private Instant createdAt;
}
