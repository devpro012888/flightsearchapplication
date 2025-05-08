package com.flightsearch.flight_app.service;

import com.flightsearch.flight_app.model.Flight;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FlightServiceTest {

    @InjectMocks
    private FlightService flightService;

    @Mock
    private RestTemplate restTemplate;

    @Value("${aviationstack.api.key}")
    private String apiKey;

    @Test
    void testFetchCurrentFlights() {
        MockitoAnnotations.openMocks(this);

        // Mock API response
        String mockResponse = "{\"data\": [{\"departure\": {\"airport\": \"JFK\", \"scheduled\": \"2025-05-08T10:00:00Z\"}, \"arrival\": {\"airport\": \"LAX\", \"scheduled\": \"2025-05-08T13:00:00Z\"}}]}";
        String url = "https://api.aviationstack.com/v1/flights?access_key=" + apiKey;

        when(restTemplate.getForObject(url, String.class)).thenReturn(mockResponse);

        // Call the method
        List<Flight> flights = flightService.fetchCurrentFlights();

        // Verify the results
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals("JFK", flights.get(0).getDeparture());
        assertEquals("LAX", flights.get(0).getDestination());
    }
}
