package com.colak.springtutorial.controller;

import com.colak.springtutorial.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@Valid
                                             @FileTypeRestriction(acceptedTypes = {MediaType.TEXT_PLAIN_VALUE, MediaType.IMAGE_PNG_VALUE})
                                             @RequestParam("file") MultipartFile file) {
        try {
            fileStorageService.storeFle(file);

            String originalFilename = file.getOriginalFilename();
            return ResponseEntity.ok("File uploaded successfully: " + originalFilename);
        } catch (IOException exception) {
            return ResponseEntity.internalServerError().body("Failed to store file " + file.getOriginalFilename());
        }
    }


    // // See https://blog.devops.dev/spring-boot-file-upload-download-delete-94982145bea0
    // The :.+ regex pattern allows the path variable to include dots (e.g., "file.txt" or "document.pdf").
    @DeleteMapping("/delete/{fileName:.+}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        try {
            if (fileStorageService.deleteFile(fileName)) {
                return ResponseEntity.ok("File deleted successfully: " + fileName);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found: " + fileName);
            }
        } catch (IOException exception) {
            return ResponseEntity.internalServerError().body("Failed to delete file  " + fileName);
        }
    }
}
