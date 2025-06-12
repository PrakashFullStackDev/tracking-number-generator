package com.example.trackingapi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.trackingapi.service.TrackingNumberService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrackingNumberServiceTest {

    @Autowired
    private TrackingNumberService service;

    @Test
    void generatesUniqueNumbers() throws Exception {
        String num1 = service.generateTrackingNumber("US", "UK", 1.0).get();
        String num2 = service.generateTrackingNumber("US", "UK", 1.0).get();
        assertNotEquals(num1, num2); // Verify uniqueness
    }

    @Test
    void validatesCountryCodes() {
        assertThrows(Exception.class, () -> 
            service.generateTrackingNumber("USA", "UK", 1.0));
    }

    @Test
    void rejectsNegativeWeight() {
        assertThrows(Exception.class, () ->
            service.generateTrackingNumber("US", "UK", -1.0));
    }
}