package com.example.trackingapi.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.trackingapi.service.TrackingNumberService;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(TrackingNumberService.class);

    @ExceptionHandler(InvalidCountryCodeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCountry(InvalidCountryCodeException ex) {
    	log.info("InvalidCountryCodeException");
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Invalid country code",
            "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(InvalidWeightException.class)
    public ResponseEntity<Map<String, String>> handleInvalidWeight(InvalidWeightException ex) {
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Invalid weight",
            "message", ex.getMessage()
        ));
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
            .collect(Collectors.toMap(
                v -> v.getPropertyPath().toString(),
                v -> v.getMessage()
            ));
        return ResponseEntity.badRequest().body(errors);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleRequestParameterError(MissingServletRequestParameterException ex) {
    	log.info("Internal Server Error");
        return ResponseEntity.internalServerError().body(Map.of(
            "error", "Required Parameter is not present",
            "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericError(Exception ex) {
    	log.info("Internal Server Error");
        return ResponseEntity.internalServerError().body(Map.of(
            "error", "Internal server error",
            "message", ex.getMessage()
        ));
    }
}