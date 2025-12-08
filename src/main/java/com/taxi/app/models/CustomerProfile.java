package com.taxi.app.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // базовый адрес (например, дом/гараж, куда чаще всего вызывают технику)
    private String defaultAddress;

    // Фото удостоверения клиента (если нужно подтверждение личности)
    private String idFrontUrl;
    private String idBackUrl;
}
