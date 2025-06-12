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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        when(trackingService.generateTrackingNumber(anyString(), anyString(), anyDouble()))
            .thenReturn(CompletableFuture.completedFuture("TN-US-UK-12345"));

        mockMvc.perform(get("/api/tracking-number")
                .param("originCountry", "US")
                .param("destinationCountry", "UK")
                .param("weight", "1.5")
                .param("createdAt", "2023-01-01T00:00:00Z")
                .param("customerId", testCustomerId.toString())
                .param("customerName", "Test Customer"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void getTrackingNumber_InvalidCountryCode_Returns400() throws Exception {
        // Simulate what would happen if invalid input was caught
        when(trackingService.generateTrackingNumber(eq("USAAAAA"), anyString(), anyDouble()))
            .thenThrow(new IllegalArgumentException("originCountry must be a valid 2-letter ISO country code"));

        mockMvc.perform(get("/api/tracking-number")
                .param("originCountry", "USAAAAA") // Invalid
                .param("destinationCountry", "UK")
                .param("weight", "1.5")
                .param("createdAt", "2023-01-01T00:00:00Z")
                .param("customerId", testCustomerId.toString())
                .param("customerName", "Test Customer"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(containsString("2-letter ISO country code")));
    }
}
