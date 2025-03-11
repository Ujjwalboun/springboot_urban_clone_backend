package org.example.controller;

import org.example.model.Customer;
import org.example.model.ServiceProvider;
import org.example.payload.request.ServiceRequestCreateRequest;
import org.example.payload.response.MessageResponse;
import org.example.payload.response.ServiceRequestResponse;
import org.example.repository.CustomerRepository;
import org.example.repository.ServiceProviderRepository;
import org.example.security.services.UserDetailsImpl;
import org.example.service.ServiceRequestService;
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
@RequestMapping("/api/service-requests")
public class ServiceRequestController {

    @Autowired
    private ServiceRequestService serviceRequestService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> createServiceRequest(@Valid @RequestBody ServiceRequestCreateRequest request) {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Find the customer
            Customer customer = customerRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

            // Create the service request
            ServiceRequestResponse response = serviceRequestService.createServiceRequest(request, customer);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SERVICE_PROVIDER')")
    public ResponseEntity<?> getServiceRequestById(@PathVariable Long id) {
        try {
            ServiceRequestResponse response = serviceRequestService.getServiceRequestById(id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> getCustomerServiceRequests() {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Find the customer
            Customer customer = customerRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

            // Get service requests for the customer
            List<ServiceRequestResponse> responses = serviceRequestService.getServiceRequestsByCustomer(customer);

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/provider/available")
    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    public ResponseEntity<?> getAvailableServiceRequests() {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Find the service provider
            ServiceProvider serviceProvider = serviceProviderRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Service provider not found"));

            // Get service requests for the provider's category
            List<ServiceRequestResponse> responses = serviceRequestService.getServiceRequestsForProvider(serviceProvider);

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/provider/notified")
    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    public ResponseEntity<?> getNotifiedServiceRequests() {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Find the service provider
            ServiceProvider serviceProvider = serviceProviderRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Service provider not found"));

            // Get service requests notified to the provider
            List<ServiceRequestResponse> responses = serviceRequestService.getServiceRequestsNotifiedToProvider(serviceProvider);

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasRole('ROLE_SERVICE_PROVIDER')")
    public ResponseEntity<?> acceptServiceRequest(@PathVariable Long id) {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Find the service provider
            ServiceProvider serviceProvider = serviceProviderRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Service provider not found"));

            // Accept the service request
            ServiceRequestResponse response = serviceRequestService.acceptServiceRequest(id, serviceProvider);

            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}