package com.flightsearch.flight_app.controller;

import com.flightsearch.flight_app.model.Flight; // Adjusted package name to 'model' if that's the correct location
import com.flightsearch.flight_app.service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * Endpoint to search for flights.
     *
     * @param departure      Departure city
     * @param destination    Destination city
     * @param departureDate  Departure date
     * @param returnDate     Return date
     * @param passengers     Number of passengers
     * @return List of matching flights
     */
    @GetMapping("/search")
    public List<Flight> searchFlights(
            @RequestParam String departure,
            @RequestParam String destination,
            @RequestParam String departureDate,
            @RequestParam String returnDate,
            @RequestParam int passengers) {
        return flightService.searchFlights(departure, destination, departureDate, returnDate, passengers);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestParam String username, @RequestParam String password) {
        boolean isAuthenticated = flightService.authenticateUser(username, password);
        if (isAuthenticated) {
            return ResponseEntity.ok("User authenticated successfully.");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String username, @RequestParam String password) {
        boolean isRegistered = flightService.registerUser(username, password);
        if (isRegistered) {
            return ResponseEntity.ok("User registered successfully.");
        } else {
            return ResponseEntity.status(400).body("User registration failed. Username may already exist.");
        }
    }
}