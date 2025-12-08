package com.taxi.app.repos;

import com.taxi.app.models.DocumentSide;
import com.taxi.app.models.DocumentType;
import com.taxi.app.models.DriverDocument;
import com.taxi.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverDocumentRepository extends JpaRepository<DriverDocument, Long> {

    List<DriverDocument> findByDriver(User driver);

    Optional<DriverDocument> findByDriverAndDocumentTypeAndSide(
            User driver,
            DocumentType documentType,
            DocumentSide side
    );
}
