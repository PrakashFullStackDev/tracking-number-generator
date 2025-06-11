
package com.example.trackingapi.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TrackingNumberService {

    private final AtomicInteger counter = new AtomicInteger(0);
    private String currentDate = LocalDate.now().toString();

    @Cacheable("trackingNumbers")
    public String generateUniqueTrackingNumber() {
    	long num = counter.incrementAndGet();
        return String.format("TN-%04d-%06d", num % 10000, System.currentTimeMillis() % 1000000);
    }
    
    public LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now();
    }
}
