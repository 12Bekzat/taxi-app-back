package com.taxi.app.controllers;

import com.taxi.app.dtos.DriverVehicleDto;
import com.taxi.app.models.DriverVehicle;
import com.taxi.app.models.User;
import com.taxi.app.repos.DriverVehicleRepository;
import com.taxi.app.services.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/driver/vehicle")
public class DriverVehicleController {

    private final DriverVehicleRepository vehicleRepository;
    private final FileStorageService fileStorageService;
    private final String filesBaseUrl;

    public DriverVehicleController(
            DriverVehicleRepository vehicleRepository,
            FileStorageService fileStorageService,
            @Value("${app.files-base-url}") String filesBaseUrl
    ) {
        this.vehicleRepository = vehicleRepository;
        this.fileStorageService = fileStorageService;
        this.filesBaseUrl = filesBaseUrl;
    }

    @GetMapping
    public ResponseEntity<DriverVehicleDto> getMyVehicle(@AuthenticationPrincipal User driver) {
        return vehicleRepository.findByDriver(driver)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public DriverVehicleDto saveVehicleMultipart(
            @RequestParam Integer equipmentTypeId,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer year,
            @RequestPart(required = false) MultipartFile photo,
            @AuthenticationPrincipal User driver
    ) {
        DriverVehicle vehicle = vehicleRepository.findByDriver(driver)
                .orElseGet(() -> {
                    DriverVehicle v = new DriverVehicle();
                    v.setDriver(driver);
                    return v;
                });

        vehicle.setEquipmentTypeId(equipmentTypeId);
        vehicle.setModel(model);
        vehicle.setPlateNumber(plateNumber);
        vehicle.setColor(color);
        vehicle.setYear(year);

        if (photo != null && !photo.isEmpty()) {
            String path = fileStorageService.store(photo, "vehicles", "vehicle-" + driver.getId());
            vehicle.setPhotoPath(path);
        }

        DriverVehicle saved = vehicleRepository.save(vehicle);
        return toDto(saved);
    }

    // если хочешь принимать JSON без файла:
    // @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    // public DriverVehicleDto saveVehicleJson(@RequestBody DriverVehicleDto dto) { ... }

    private DriverVehicleDto toDto(DriverVehicle v) {
        DriverVehicleDto dto = new DriverVehicleDto();
        dto.setId(v.getId());
        dto.setEquipmentTypeId(v.getEquipmentTypeId());
        dto.setModel(v.getModel());
        dto.setPlateNumber(v.getPlateNumber());
        dto.setColor(v.getColor());
        dto.setYear(v.getYear());
        if (v.getPhotoPath() != null) {
            dto.setPhotoUrl(filesBaseUrl + "/" + v.getPhotoPath());
        }
        // equipmentName можешь подтягивать из справочника типов техники
        dto.setEquipmentName(null);
        return dto;
    }
}

