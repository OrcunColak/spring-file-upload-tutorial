package com.colak.springtutorial.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.PARAMETER}) // Target same as the main annotation
@Retention(RetentionPolicy.RUNTIME) // Retain at runtime
public @interface FileTypeRestrictions {
    FileTypeRestriction[] value(); // Array of FileTypeRestriction annotations
}
