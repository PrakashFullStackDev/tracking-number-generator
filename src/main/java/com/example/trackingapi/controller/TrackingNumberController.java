package com.example.trackingapi.controller;

import com.example.trackingapi.model.TrackingResponse;
import com.example.trackingapi.service.TrackingNumberService;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tracking-number")
public class TrackingNumberController {

    private final TrackingNumberService trackingService;

    public TrackingNumberController(TrackingNumberService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping
    public Map<String, Object> getTrackingNumber(
            @RequestParam String originCountry,
            @RequestParam String destinationCountry,
            @RequestParam double weight,
            @RequestParam String createdAt,
            @RequestParam UUID customerId,
            @RequestParam String customerName) {
        String trackingNumber = trackingService.generateUniqueTrackingNumber();
        return Map.of(
                "tracking_number", trackingNumber,
                "created_at", Instant.now().toString(),
                "origin_country", originCountry,
                "destination_country", destinationCountry,
                "weight", weight,
                "customer_id", customerId.toString(),
                "customer_name", customerName
            );
        
    }    
}
