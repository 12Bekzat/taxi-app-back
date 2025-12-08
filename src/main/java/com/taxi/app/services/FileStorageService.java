package com.taxi.app.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation;

    public FileStorageService(@Value("${app.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file, String subDir, String prefix) {
        if (file.isEmpty()) {
            throw new RuntimeException("Cannot store empty file");
        }
        try {
            String ext = getExtension(file.getOriginalFilename());
            String filename = prefix + "-" + System.currentTimeMillis() + "-" + UUID.randomUUID() + ext;

            Path dir = rootLocation.resolve(subDir);
            Files.createDirectories(dir);

            Path dest = dir.resolve(filename);
            Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

            // относительный путь для URL
            return subDir + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    private String getExtension(String originalName) {
        if (!StringUtils.hasText(originalName)) return "";
        int idx = originalName.lastIndexOf('.');
        return idx >= 0 ? originalName.substring(idx) : "";
    }
}
