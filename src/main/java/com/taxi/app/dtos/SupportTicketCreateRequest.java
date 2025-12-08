package com.taxi.app.dtos;

import lombok.Data;

@Data
public class SupportTicketCreateRequest {
    private String subject;
    private String message;
}
