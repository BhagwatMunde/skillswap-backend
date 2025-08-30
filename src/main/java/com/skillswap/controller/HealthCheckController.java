package com.skillswap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health") // Base path for health endpoints
public class HealthCheckController {

    /**
     * Simple endpoint to check if the application is running.
     * Accessible without authentication.
     * Returns "Application is running!" with a 200 OK status.
     */
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Application is running!");
    }
}
