package com.taxi.app.repos;

import com.taxi.app.models.DriverVehicle;
import com.taxi.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverVehicleRepository extends JpaRepository<DriverVehicle, Long> {
    Optional<DriverVehicle> findByDriver(User driver);
}
