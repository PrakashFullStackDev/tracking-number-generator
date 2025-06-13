
package com.example.trackingapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.trackingapi.exceptions.InvalidCountryCodeException;
import com.example.trackingapi.exceptions.InvalidWeightException;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class TrackingNumberService {
	
	private static final Logger log = LoggerFactory.getLogger(TrackingNumberService.class);

    private final AtomicInteger counter = new AtomicInteger(100000);
    private String currentDate = LocalDate.now().toString();

        @Async
        @Cacheable(value = "trackingNumbers", key = "{#origin, #dest, #weight}")
        public CompletableFuture<String> generateTrackingNumber(
                @NotNull String origin, 
                @NotNull String dest, 
                @Positive double weight) {
            log.info("Start method of generateTrackingNumber");
            // Validate inputs using custom exceptions
            if (origin == null || !origin.matches("^[A-Z]{2}$") || !StringUtils.hasLength(origin)) {
                throw new InvalidCountryCodeException("Origin must be a 2-letter ISO country code");
            }
            if (dest == null || !dest.matches("^[A-Z]{2}$")) {
                throw new InvalidCountryCodeException("Destination must be a 2-letter ISO country code");
            }
            if (weight <= 0) {
                throw new InvalidWeightException("Weight must be positive");
            }
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();
           log.info(String.format("TN-%s-%s-%s", 
                    origin, dest, randomUUIDString));
            return CompletableFuture.completedFuture(
                String.format("TN-%s-%s-%s", 
                    origin, dest, randomUUIDString)
            );
        }
    
    private void validateInputs(String origin, String dest, double weight) {
        if (origin == null || origin.length() != 2) {
            throw new IllegalArgumentException("Origin must be 2-letter country code");
        }
        if (dest == null || dest.length() != 2) {
            throw new IllegalArgumentException("Destination must be 2-letter country code");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
    }
}
