package com.taxi.app.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "special_equipment_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialEquipmentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;           // EVAC, MANIP, CRANE...

    @Column(nullable = false)
    private String name;           // "Эвакуатор", "Манипулятор"

    @Column
    private String description;

    @Column(nullable = false)
    private int defaultMinutes;    // базовая длительность, например 30 мин

    @Column(nullable = false)
    private long basePricePerMinute; // в тынах за минуту, до коэффициентов

    @Column(nullable = false)
    private boolean active = true;
}

