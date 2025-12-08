package com.taxi.app.models;

// domain/driver/DriverDocument.java
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(
        name = "driver_documents",
        uniqueConstraints = @UniqueConstraint(columnNames = {"driver_id", "document_type", "side"})
)
public class DriverDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 50)
    private DocumentType documentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "side", nullable = false, length = 10)
    private DocumentSide side;

    @Column(name = "file_path", nullable = false, length = 512)
    private String filePath;

    @Column(name = "uploaded_at")
    private Instant uploadedAt = Instant.now();

    // getters/setters
}
