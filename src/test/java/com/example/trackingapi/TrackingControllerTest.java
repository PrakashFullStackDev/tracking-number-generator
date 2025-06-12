package com.example.trackingapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.trackingapi.service.TrackingNumberService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TrackingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackingNumberService trackingService;

    private final UUID testCustomerId = UUID.randomUUID();
    
    @Test
    void getTrackingNumber_ValidRequest_Returns200() throws Exception {
        // Mock service response
        when(trackingService.generateTrackingNumber(anyString(), anyString(), anyDouble()))
            .thenReturn(CompletableFuture.completedFuture("TN-US-UK-12345"));

        mockMvc.perform(get("/api/tracking-number")
                .param("originCountry", "US")
                .param("destinationCountry", "UK")
                .param("weight", "1.5")
                .param("createdAt", "2023-01-01T00:00:00Z")
                .param("customerId", testCustomerId.toString())
                .param("customerName", "Test Customer"))
            
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tracking_number").value("TN-US-UK-12345"))
            .andExpect(jsonPath("$.origin_country").value("US"))
            .andExpect(jsonPath("$.destination_country").value("UK"))
            .andExpect(jsonPath("$.weight").value(1.5))
            .andExpect(jsonPath("$.customer_id").value(testCustomerId.toString()))
            .andExpect(jsonPath("$.customer_name").value("Test Customer"))
            .andExpect(jsonPath("$.created_at").exists()); // Checks if timestamp exists
    }
    
    
    @Test
    void getTrackingNumber_InvalidCountryCode_Returns400() throws Exception {
        mockMvc.perform(get("/api/tracking-number")
                .param("originCountry", "USAAAAA") // Invalid
                .param("destinationCountry", "UK")
                .param("weight", "1.5")
                .param("createdAt", "2023-01-01T00:00:00Z")
                .param("customerId", testCustomerId.toString())
                .param("customerName", "Test Customer"))
            
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.[*].find { it.field == 'originCountry' }.defaultMessage")
                     .value(containsString("Must be 2-letter ISO country code")));
    }
    }
