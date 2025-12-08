package com.taxi.app.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // телефон — обязательный и уникальный
    @Column(nullable = false, unique = true)
    private String phone;

    // email — опциональный, можешь оставить unique если хочешь,
    // тогда два пользователя не смогут использовать один email
    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // общее для всех
    private String firstName;
    private String lastName;
    private String avatarUrl;
}