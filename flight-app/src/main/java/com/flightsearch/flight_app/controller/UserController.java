package com.flightsearch.flight_app.controller;

import com.flightsearch.flight_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");

        boolean isRegistered = userService.registerUser(username, password);
        if (isRegistered) {
            return ResponseEntity.ok("User registered successfully.");
        } else {
            return ResponseEntity.badRequest().body("Registration failed.");
        }
    }
}
