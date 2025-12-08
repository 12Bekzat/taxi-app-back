package com.taxi.app.repos;

import com.taxi.app.models.DriverRating;
import com.taxi.app.models.Order;
import com.taxi.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRatingRepository extends JpaRepository<DriverRating, Long> {

    Optional<DriverRating> findByOrder(Order order);

    List<DriverRating> findByDriverOrderByCreatedAtDesc(User driver);
}
