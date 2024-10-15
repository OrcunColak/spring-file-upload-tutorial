package com.colak.springtutorial.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@Slf4j
public class FileUploadController {

    @Value("${upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            Path directory = Paths.get(uploadDir);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            String sanitizedFilename = sanitizeFileName(originalFilename);
            Path filePath = directory.resolve(sanitizedFilename);
            log.info("File path: {}", filePath);
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok("File uploaded successfully: " + originalFilename);

        } catch (IOException exception) {
            throw new FileStorageException("Failed to store file " + file.getOriginalFilename());
        }
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.\\-]", "_"); // Replaces dangerous characters with underscores
    }

    // // See https://blog.devops.dev/spring-boot-file-upload-download-delete-94982145bea0
    // The :.+ regex pattern allows the path variable to include dots (e.g., "file.txt" or "document.pdf").
    @DeleteMapping("/delete/{fileName:.+}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        try {
            Path directory = Paths.get(uploadDir);
            Path filePath = directory.resolve(fileName).normalize();
            log.info("Delete file path: {}", fileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return ResponseEntity.ok("File deleted successfully: " + fileName);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found: " + fileName);
            }
        } catch (IOException e) {
            throw new FileStorageException("Failed to delete file " + fileName, e);
        }
    }
}
