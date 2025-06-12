package com.example.trackingapi.controller;

import com.example.trackingapi.exceptions.InvalidCountryCodeException;
import com.example.trackingapi.exceptions.InvalidWeightException;
import com.example.trackingapi.exceptions.TrackingNumberGenerationException;
import com.example.trackingapi.model.TrackingResponse;
import com.example.trackingapi.service.TrackingNumberService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api")
public class TrackingNumberController {
	
	private static final Logger log = LoggerFactory.getLogger(TrackingNumberService.class);

    private final TrackingNumberService trackingService;

    public TrackingNumberController(TrackingNumberService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping("/tracking-numberr")
    public CompletableFuture<Map<String, Object>> getTrackingNumberr(
            @RequestParam 
            @Pattern(regexp = "^[A-Z]{2}$", message = "Must be 2-letter ISO country code") 
            String originCountry,
            
            @RequestParam 
            @Pattern(regexp = "^[A-Z]{2}$", message = "Must be 2-letter ISO country code") 
            String destinationCountry,
            
            @RequestParam 
            @Positive(message = "Weight must be positive") 
            double weight,
            
            @RequestParam 
            @NotNull 
            String createdAt,
            
            @RequestParam 
            @NotNull 
            UUID customerId,
            
            @RequestParam 
            @NotBlank 
            String customerName) {
    	
    	log.info("Controller method started ");
        
        CompletableFuture<String> trackingNumberFuture = trackingService.generateTrackingNumber(
            originCountry, destinationCountry, weight);
        
        return trackingNumberFuture
            .thenApply(trackingNumber -> {
                return Map.<String, Object>of(
                    "tracking_number", trackingNumber,
                    "created_at", Instant.now().toString(),
                    "origin_country", originCountry,
                    "destination_country", destinationCountry,
                    "weight", weight,
                    "customer_id", customerId.toString(),
                    "customer_name", customerName
                );
            })
            .exceptionally(ex -> {
                return Map.<String, Object>of(
                    "error", "Failed to generate tracking number",
                    "message", ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage(),
                    "status", "ERROR"
                );
            });
    }
    @GetMapping("/tracking-number")
    public CompletableFuture<Map<String, Object>> getTrackingNumber(
            @RequestParam @Pattern(regexp = "^[A-Z]{2}$") String originCountry,
            @RequestParam @Pattern(regexp = "^[A-Z]{2}$") String destinationCountry,
            @RequestParam @Positive double weight,
            @RequestParam @NotNull String createdAt,
            @RequestParam @NotNull UUID customerId,
            @RequestParam @NotBlank String customerName) {
    	log.info("Controller method started ");
        return trackingService.generateTrackingNumber(originCountry, destinationCountry, weight)
            .thenApply(trackingNumber -> {
                Map<String, Object> response = new HashMap<>();
                response.put("tracking_number", trackingNumber);
                response.put("created_at", Instant.now().toString());
                response.put("origin_country", originCountry);
                response.put("destination_country", destinationCountry);
                response.put("weight", weight);
                response.put("customer_id", customerId.toString());
                response.put("customer_name", customerName);
                return response;
            })
            .exceptionally(ex -> {
                Map<String, Object> errorResponse = new HashMap<>();
                if (ex.getCause() instanceof InvalidCountryCodeException) {
                	log.error(ex.getMessage());
                    errorResponse.put("error", "Invalid country code");
                    errorResponse.put("message", ex.getCause().getMessage());
                } else if (ex.getCause() instanceof InvalidWeightException) {
                	log.error(ex.getMessage());
                    errorResponse.put("error", "Invalid weight");
                    errorResponse.put("message", ex.getCause().getMessage());
                } else {
                	log.error(ex.getMessage());
                    errorResponse.put("error", "Internal server error");
                    errorResponse.put("message", "Failed to generate tracking number");
                }
                return errorResponse;
            });
    }    
}
