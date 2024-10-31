package com.colak.springtutorial.controller;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
public class FileTypeRestrictionValidator implements ConstraintValidator<FileTypeRestriction, Object> {

    private List<String> acceptedTypes;
    private String messageTemplate;

    @Override
    public void initialize(FileTypeRestriction annotation) {
        acceptedTypes = List.of(annotation.acceptedTypes()); // Convert array to List
        messageTemplate = annotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are valid
        }

        boolean isFile = value instanceof MultipartFile;
        boolean isList = value instanceof List<?>; // Use wildcard for list
        boolean isArray = value.getClass().isArray(); // Check if it's an array

        if (isFile) {
            return validateFile((MultipartFile) value, context);
        } else if (isList) {
            List<?> files = (List<?>) value;
            return files.stream().allMatch(file -> validateFile((MultipartFile) file, context));
        } else if (isArray) {
            Object[] filesArray = (Object[]) value;
            for (Object file : filesArray) {
                if (!validateFile((MultipartFile) file, context)) {
                    return false;
                }
            }
            return true;
        }

        return true; // Not a file or list, treat as valid
    }

    private boolean validateFile(MultipartFile file, ConstraintValidatorContext context) {
        String contentType = detectContentType(file);
        boolean isValid = acceptedTypes.contains(contentType);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            String.format(messageTemplate, file.getOriginalFilename(), acceptedTypes))
                    .addConstraintViolation();
        }

        return isValid;
    }

    private String detectContentType(MultipartFile file) {
        // Implement the content type detection logic
        Detector detector = new DefaultDetector();
        Metadata metadata = new Metadata();

        String result = "";
        try {
            BufferedInputStream inputStream = new BufferedInputStream(file.getInputStream());
            MediaType mediaType = detector.detect(inputStream, metadata);
            result = mediaType.toString();
        } catch (IOException exception) {
            log.error("Exception caught :", exception);
        }
        return result;
    }
}

