package org.example.controller;

import org.example.model.ServiceProvider;
import org.example.payload.response.MessageResponse;
import org.example.payload.response.NotificationCountResponse;
import org.example.payload.response.NotificationResponse;
import org.example.repository.ServiceProviderRepository;
import org.example.security.services.UserDetailsImpl;
import org.example.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    public ResponseEntity<?> getAllNotifications() {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Find the service provider
            ServiceProvider serviceProvider = serviceProviderRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Service provider not found"));

            // Get all notifications for the service provider
            List<NotificationResponse> notifications = notificationService.getNotificationsForServiceProvider(serviceProvider);

            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/unread")
    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    public ResponseEntity<?> getUnreadNotifications() {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Find the service provider
            ServiceProvider serviceProvider = serviceProviderRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Service provider not found"));

            // Get unread notifications for the service provider
            List<NotificationResponse> notifications = notificationService.getUnreadNotificationsForServiceProvider(serviceProvider);

            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    public ResponseEntity<?> getNotificationCount() {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Find the service provider
            ServiceProvider serviceProvider = serviceProviderRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Service provider not found"));

            // Get notification count for the service provider
            NotificationCountResponse countResponse = notificationService.getNotificationCount(serviceProvider);

            return ResponseEntity.ok(countResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long id) {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Find the service provider
            ServiceProvider serviceProvider = serviceProviderRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Service provider not found"));

            // Mark notification as read
            NotificationResponse notification = notificationService.markNotificationAsRead(id, serviceProvider);

            return ResponseEntity.ok(notification);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PutMapping("/read-all")
    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    public ResponseEntity<?> markAllNotificationsAsRead() {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Find the service provider
            ServiceProvider serviceProvider = serviceProviderRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Service provider not found"));

            // Mark all notifications as read
            notificationService.markAllNotificationsAsRead(serviceProvider);

            return ResponseEntity.ok(new MessageResponse("All notifications marked as read"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}