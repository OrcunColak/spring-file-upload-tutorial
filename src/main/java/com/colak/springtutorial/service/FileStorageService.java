package com.colak.springtutorial.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@Slf4j
public class FileStorageService {

    @Value("${upload-dir}")
    private String uploadDir;

    private Path directory;

    @PostConstruct
    void postConstruct() throws IOException {
        this.directory = Paths.get(uploadDir);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }

    public void storeFle(MultipartFile file) throws IOException {
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        String sanitizedFilename = sanitizeFileName(originalFilename);
        Path filePath = directory.resolve(sanitizedFilename);
        log.info("File path: {}", filePath);

        Files.write(filePath, file.getBytes());
    }

    public boolean deleteFile(@PathVariable String fileName) throws IOException {
        boolean result = false;
        String sanitizedFilename = sanitizeFileName(fileName);
        Path filePath = directory.resolve(sanitizedFilename).normalize();
        log.info("Delete file path: {}", sanitizedFilename);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            result = true;
        }
        return result;

    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.\\-]", "_"); // Replaces dangerous characters with underscores
    }
}
