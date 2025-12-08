package com.taxi.app.repos;

import com.taxi.app.models.SupportTicket;
import com.taxi.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByUserOrderByCreatedAtDesc(User user);
}

