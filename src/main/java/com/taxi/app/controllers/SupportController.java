package com.taxi.app.controllers;

import com.taxi.app.dtos.SupportTicketCreateRequest;
import com.taxi.app.dtos.SupportTicketResponse;
import com.taxi.app.models.SupportStatus;
import com.taxi.app.models.SupportTicket;
import com.taxi.app.models.User;
import com.taxi.app.repos.SupportTicketRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
public class SupportController {

    private final SupportTicketRepository supportTicketRepository;

    public SupportController(SupportTicketRepository supportTicketRepository) {
        this.supportTicketRepository = supportTicketRepository;
    }

    @PostMapping
    public ResponseEntity<SupportTicketResponse> createTicket(
            @RequestBody SupportTicketCreateRequest request,
            @AuthenticationPrincipal User user
    ) {

        if (request.getSubject() == null || request.getSubject().isBlank()
                || request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        SupportTicket ticket = new SupportTicket();
        ticket.setUser(user);
        ticket.setSubject(request.getSubject().trim());
        ticket.setMessage(request.getMessage().trim());
        ticket.setStatus(SupportStatus.OPEN);

        SupportTicket saved = supportTicketRepository.save(ticket);

        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping("/my")
    public List<SupportTicketResponse> getMyTickets(@AuthenticationPrincipal User user) {
        return supportTicketRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private SupportTicketResponse toResponse(SupportTicket t) {
        SupportTicketResponse dto = new SupportTicketResponse();
        dto.setId(t.getId());
        dto.setSubject(t.getSubject());
        dto.setMessage(t.getMessage());
        dto.setStatus(t.getStatus());
        dto.setCreatedAt(t.getCreatedAt());
        return dto;
    }
}

