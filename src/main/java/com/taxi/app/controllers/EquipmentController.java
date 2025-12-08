package com.taxi.app.controllers;

import com.taxi.app.models.SpecialEquipmentType;
import com.taxi.app.repos.SpecialEquipmentTypeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/equipment-types")
public class EquipmentController {

    private final SpecialEquipmentTypeRepository repo;

    public EquipmentController(SpecialEquipmentTypeRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<SpecialEquipmentType> list() {
        return repo.findAll();
    }
}
