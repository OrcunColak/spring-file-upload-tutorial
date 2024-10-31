package com.colak.springtutorial.controller;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Repeatable(FileTypeRestrictions.class) // Support for repeatable annotations
@Target({ElementType.TYPE, ElementType.PARAMETER}) // Equivalent to Kotlin's AnnotationTarget
@Constraint(validatedBy = {FileTypeRestrictionValidator.class}) // Validator class
@Retention(RetentionPolicy.RUNTIME) // Retain at runtime
public @interface FileTypeRestriction {
    String[] acceptedTypes(); // Property for accepted file types
    String message() default "File is not allowed"; // Default error message
    Class<?>[] groups() default {}; // Groups for validation
    Class<? extends Payload>[] payload() default {}; // Payload for additional data
}
