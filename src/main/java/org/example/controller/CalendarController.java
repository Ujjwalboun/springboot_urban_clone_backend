package org.example.controller;


import org.example.model.ServiceProvider;
import org.example.payload.request.AvailabilityRequest;
import org.example.payload.response.AvailabilityResponse;
import org.example.payload.response.MessageResponse;
import org.example.payload.response.TimeSlotResponse;
import org.example.repository.ServiceProviderRepository;
import org.example.security.services.UserDetailsImpl;
import org.example.service.CalendarService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @GetMapping("/availability")
    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    public ResponseEntity<?> getAvailability() {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Find the service provider
            ServiceProvider serviceProvider = serviceProviderRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Service provider not found"));

            // Get availability for the service provider
            List<AvailabilityResponse> responses = calendarService.getAvailabilityForServiceProvider(serviceProvider);

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/availability")
    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    public ResponseEntity<?> updateAvailability(@Valid @RequestBody AvailabilityRequest request) {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Find the service provider
            ServiceProvider serviceProvider = serviceProviderRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Service provider not found"));

            // Update availability
            AvailabilityResponse response = calendarService.updateAvailability(serviceProvider, request);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/time-slots")
    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    public ResponseEntity<?> getTimeSlots() {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Find the service provider
            ServiceProvider serviceProvider = serviceProviderRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Service provider not found"));

            // Get time slots for the service provider
            List<TimeSlotResponse> responses = calendarService.getTimeSlotsByServiceProvider(serviceProvider);

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}
