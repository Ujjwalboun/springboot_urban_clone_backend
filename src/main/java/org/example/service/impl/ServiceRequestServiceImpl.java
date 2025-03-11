package org.example.service.impl;

import org.example.logging.AuditLogger;
import org.example.model.Customer;
import org.example.model.ServiceProvider;
import org.example.model.ServiceRequest;
import org.example.model.ServiceRequestStatus;
import org.example.payload.request.ServiceRequestCreateRequest;
import org.example.payload.response.ServiceRequestResponse;
import org.example.repository.ServiceProviderRepository;
import org.example.repository.ServiceRequestRepository;
import org.example.service.CalendarService;
import org.example.service.NotificationService;
import org.example.service.ServiceRequestService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServiceRequestServiceImpl implements ServiceRequestService {

    @Autowired

    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private AuditLogger auditLogger;

    @Override
    @Transactional
    public ServiceRequestResponse createServiceRequest(ServiceRequestCreateRequest request, Customer customer) {
        // Create and save the service request
        ServiceRequest serviceRequest = new ServiceRequest(
                request.getCategory(),
                request.getDescription(),
                request.getLocation(),
                request.getPreferredDate(),
                customer
        );

        serviceRequest = serviceRequestRepository.save(serviceRequest);

        // Log service request creation
        auditLogger.logServiceRequestCreation(
                serviceRequest.getId(),
                serviceRequest.getCategory(),
                customer.getEmail());

        // Find service providers that match the category
        List<ServiceProvider> matchingProviders = serviceProviderRepository.findAll().stream()
                .filter(provider -> provider.getServiceType().equalsIgnoreCase(request.getCategory()))
                .collect(Collectors.toList());

        // Notify matching service providers
        if (!matchingProviders.isEmpty()) {
            notificationService.notifyServiceProviders(serviceRequest, matchingProviders);
            // Save the service request again to update the notified providers
            serviceRequest = serviceRequestRepository.save(serviceRequest);
        }

        // Convert to response DTO
        return convertToResponseDto(serviceRequest);
    }

    @Override
    public ServiceRequestResponse getServiceRequestById(Long id) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service request not found with id: " + id));

        return convertToResponseDto(serviceRequest);
    }

    @Override
    public List<ServiceRequestResponse> getServiceRequestsByCustomer(Customer customer) {
        List<ServiceRequest> serviceRequests = serviceRequestRepository.findByCustomer(customer);

        return serviceRequests.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceRequestResponse> getServiceRequestsForProvider(ServiceProvider serviceProvider) {
        List<ServiceRequest> serviceRequests = serviceRequestRepository.findByCategory(serviceProvider.getServiceType());

        return serviceRequests.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceRequestResponse> getServiceRequestsNotifiedToProvider(ServiceProvider serviceProvider) {
        List<ServiceRequest> serviceRequests = serviceRequestRepository.findByNotifiedProvidersContaining(serviceProvider);

        return serviceRequests.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ServiceRequestResponse acceptServiceRequest(Long requestId, ServiceProvider serviceProvider) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Service request not found with id: " + requestId));

        // Check if the service provider is in the notified list
        if (!serviceRequest.getNotifiedProviders().contains(serviceProvider)) {
            throw new IllegalStateException("Service provider not notified for this request");
        }

        // Check if the request is still pending
        if (serviceRequest.getStatus() != ServiceRequestStatus.PENDING) {
            throw new IllegalStateException("Service request is already " + serviceRequest.getStatus());
        }

        // Check if the service provider is available at the requested time
        LocalDateTime startTime = serviceRequest.getPreferredDate();
        // Assuming service takes 2 hours
        LocalDateTime endTime = startTime.plus(2, ChronoUnit.HOURS);

        if (!calendarService.isServiceProviderAvailable(serviceProvider, startTime, endTime)) {
            throw new IllegalStateException("Service provider is not available at the requested time");
        }

        // Update the service request
        serviceRequest.setAcceptedProvider(serviceProvider);
        serviceRequest.setStatus(ServiceRequestStatus.ASSIGNED);

        // Block the time slot in the service provider's calendar
        calendarService.blockTimeSlot(serviceProvider, serviceRequest, startTime, endTime);

        // Cancel notifications to other service providers
        calendarService.cancelServiceRequestNotifications(serviceRequest);

        serviceRequest = serviceRequestRepository.save(serviceRequest);

        // Log service request acceptance
        auditLogger.logServiceRequestAcceptance(serviceRequest.getId(), serviceProvider.getEmail());

        return convertToResponseDto(serviceRequest);
    }

    // Helper method to convert ServiceRequest to ServiceRequestResponse
    private ServiceRequestResponse convertToResponseDto(ServiceRequest serviceRequest) {
        ServiceRequestResponse response = new ServiceRequestResponse();
        response.setId(serviceRequest.getId());
        response.setCategory(serviceRequest.getCategory());
        response.setDescription(serviceRequest.getDescription());
        response.setLocation(serviceRequest.getLocation());
        response.setPreferredDate(serviceRequest.getPreferredDate());
        response.setStatus(serviceRequest.getStatus());
        response.setCreatedAt(serviceRequest.getCreatedAt());

        // Set customer info
        Customer customer = serviceRequest.getCustomer();
        ServiceRequestResponse.CustomerInfoResponse customerInfo = new ServiceRequestResponse.CustomerInfoResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone()
        );
        response.setCustomer(customerInfo);

        // Set notified providers info
        Set<ServiceRequestResponse.ServiceProviderInfoResponse> notifiedProvidersInfo = new HashSet<>();
        for (ServiceProvider provider : serviceRequest.getNotifiedProviders()) {
            notifiedProvidersInfo.add(new ServiceRequestResponse.ServiceProviderInfoResponse(
                    provider.getId(),
                    provider.getName(),
                    provider.getEmail(),
                    provider.getPhone(),
                    provider.getServiceType()
            ));
        }
        response.setNotifiedProviders(notifiedProvidersInfo);

        // Set accepted provider info if available
        if (serviceRequest.getAcceptedProvider() != null) {
            ServiceProvider acceptedProvider = serviceRequest.getAcceptedProvider();
            ServiceRequestResponse.ServiceProviderInfoResponse acceptedProviderInfo =
                    new ServiceRequestResponse.ServiceProviderInfoResponse(
                            acceptedProvider.getId(),
                            acceptedProvider.getName(),
                            acceptedProvider.getEmail(),
                            acceptedProvider.getPhone(),
                            acceptedProvider.getServiceType()
                    );
            response.setAcceptedProvider(acceptedProviderInfo);
        }

        return response;
    }
}
