package com.taxi.app.controllers;

import com.taxi.app.dtos.DriverDocumentDto;
import com.taxi.app.models.DocumentSide;
import com.taxi.app.models.DocumentType;
import com.taxi.app.models.DriverDocument;
import com.taxi.app.models.User;
import com.taxi.app.repos.DriverDocumentRepository;
import com.taxi.app.services.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/driver/documents")
public class DriverDocumentsController {

    private final DriverDocumentRepository documentRepository;
    private final FileStorageService fileStorageService;
    private final String filesBaseUrl;

    public DriverDocumentsController(
            DriverDocumentRepository documentRepository,
            FileStorageService fileStorageService,
            @Value("${app.files-base-url}") String filesBaseUrl
    ) {
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
        this.filesBaseUrl = filesBaseUrl;
    }

    @GetMapping
    public List<DriverDocumentDto> getMyDocuments(@AuthenticationPrincipal User driver) {
        return documentRepository.findByDriver(driver)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<DriverDocumentDto> uploadDocument(
            @RequestParam("documentType") DocumentType documentType,
            @RequestParam("side") DocumentSide side,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal User driver
    ) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String path = fileStorageService.store(
                file,
                "driver-docs",
                "doc-" + driver.getId() + "-" + documentType + "-" + side
        );

        DriverDocument doc = documentRepository
                .findByDriverAndDocumentTypeAndSide(driver, documentType, side)
                .orElseGet(() -> {
                    DriverDocument d = new DriverDocument();
                    d.setDriver(driver);
                    d.setDocumentType(documentType);
                    d.setSide(side);
                    return d;
                });

        doc.setFilePath(path);
        DriverDocument saved = documentRepository.save(doc);

        // здесь же можно проставить флаг driverDocumentsCompleted у User, если все 4 есть

        return ResponseEntity.ok(toDto(saved));
    }

    private DriverDocumentDto toDto(DriverDocument d) {
        DriverDocumentDto dto = new DriverDocumentDto();
        dto.setId(d.getId());
        dto.setDocumentType(d.getDocumentType());
        dto.setSide(d.getSide());
        dto.setUrl(filesBaseUrl + "/" + d.getFilePath());
        dto.setStatus("PENDING"); // пока заглушка
        return dto;
    }
}

