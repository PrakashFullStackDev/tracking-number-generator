package com.example.trackingapi.model;

import java.time.LocalDateTime;

public class TrackingResponse {
    private String trackingNumber;
    private LocalDateTime createdAt;

    public TrackingResponse(String trackingNumber, LocalDateTime createdAt) {
        this.trackingNumber = trackingNumber;
        this.createdAt = createdAt;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
