package com.taxi.app.runner;

import com.taxi.app.models.SpecialEquipmentType;
import com.taxi.app.repos.SpecialEquipmentTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {

    private final SpecialEquipmentTypeRepository equipmentRepository;

    public DataInit(SpecialEquipmentTypeRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public void run(String... args) {
        if (equipmentRepository.count() == 0) {
            equipmentRepository.save(SpecialEquipmentType.builder()
                    .code("EVAC")
                    .name("Эвакуатор")
                    .description("Стандартный эвакуатор для легковых авто")
                    .defaultMinutes(30)
                    .basePricePerMinute(600) // 600 т/мин
                    .active(true)
                    .build());

            equipmentRepository.save(SpecialEquipmentType.builder()
                    .code("MANIP")
                    .name("Манипулятор")
                    .description("Манипулятор для грузов и спецтехники")
                    .defaultMinutes(45)
                    .basePricePerMinute(800)
                    .active(true)
                    .build());

            equipmentRepository.save(SpecialEquipmentType.builder()
                    .code("TRUCK")
                    .name("Грузовой эвакуатор")
                    .description("Для грузовиков и автобусов")
                    .defaultMinutes(60)
                    .basePricePerMinute(1000)
                    .active(true)
                    .build());
        }
    }
}
