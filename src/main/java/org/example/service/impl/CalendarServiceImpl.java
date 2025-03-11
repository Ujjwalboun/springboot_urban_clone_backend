package org.example.service.impl;

import org.example.logging.AuditLogger;
import org.example.model.Availability;
import org.example.model.ServiceProvider;
import org.example.model.ServiceRequest;
import org.example.model.TimeSlot;
import org.example.payload.request.AvailabilityRequest;
import org.example.payload.response.AvailabilityResponse;
import org.example.payload.response.TimeSlotResponse;
import org.example.repository.AvailabilityRepository;
import org.example.repository.ServiceRequestRepository;
import org.example.repository.TimeSlotRepository;
import org.example.service.CalendarService;
import org.example.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl implements CalendarService {

    private static final Logger logger = LoggerFactory.getLogger(CalendarServiceImpl.class);

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuditLogger auditLogger;

    @Override
    @Transactional
    public AvailabilityResponse updateAvailability(ServiceProvider serviceProvider, AvailabilityRequest request) {
        // Check if availability already exists for this day
        Optional<Availability> existingAvailability = availabilityRepository
                .findByServiceProviderAndDayOfWeek(serviceProvider, request.getDayOfWeek());

        Availability availability;
        if (existingAvailability.isPresent()) {
            // Update existing availability
            availability = existingAvailability.get();
            availability.setStartTime(request.getStartTime());
            availability.setEndTime(request.getEndTime());
        } else {
            // Create new availability
            availability = new Availability(
                    serviceProvider,
                    request.getDayOfWeek(),
                    request.getStartTime(),
                    request.getEndTime()
            );
        }

        availability = availabilityRepository.save(availability);

        // Log availability update
        String timeRange = availability.getStartTime() + " - " + availability.getEndTime();
        auditLogger.logAvailabilityUpdate(
                serviceProvider.getEmail(),
                availability.getDayOfWeek().toString(),
                timeRange);

        return convertToAvailabilityResponse(availability);
    }

    @Override
    public List<AvailabilityResponse> getAvailabilityForServiceProvider(ServiceProvider serviceProvider) {
        List<Availability> availabilities = availabilityRepository.findByServiceProvider(serviceProvider);

        return availabilities.stream()
                .map(this::convertToAvailabilityResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeSlotResponse> getTimeSlotsByServiceProvider(ServiceProvider serviceProvider) {
        List<TimeSlot> timeSlots = timeSlotRepository.findByServiceProvider(serviceProvider);

        return timeSlots.stream()
                .map(this::convertToTimeSlotResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isServiceProviderAvailable(ServiceProvider serviceProvider, LocalDateTime startTime, LocalDateTime endTime) {
        // Check if the service provider has availability for this day of week
        DayOfWeek dayOfWeek = startTime.getDayOfWeek();
        Optional<Availability> availability = availabilityRepository
                .findByServiceProviderAndDayOfWeek(serviceProvider, dayOfWeek);

        if (availability.isEmpty()) {
            return false; // No availability set for this day
        }

        // Check if the requested time is within the service provider's availability
        LocalTime requestStartTime = startTime.toLocalTime();
        LocalTime requestEndTime = endTime.toLocalTime();

        Availability avail = availability.get();
        if (requestStartTime.isBefore(avail.getStartTime()) || requestEndTime.isAfter(avail.getEndTime())) {
            return false; // Requested time is outside availability
        }

        // Check if there are any overlapping time slots
        List<TimeSlot> overlappingSlots = timeSlotRepository.findOverlappingTimeSlots(
                serviceProvider, startTime, endTime);

        return overlappingSlots.isEmpty();
    }

    @Override
    @Transactional
    public void blockTimeSlot(ServiceProvider serviceProvider, ServiceRequest serviceRequest,
                              LocalDateTime startTime, LocalDateTime endTime) {
        // Create a new time slot
        TimeSlot timeSlot = new TimeSlot(serviceProvider, startTime, endTime, serviceRequest);
        timeSlotRepository.save(timeSlot);

        logger.info("Blocked time slot for service provider: {} from {} to {} for service request: {}",
                serviceProvider.getName(), startTime, endTime, serviceRequest.getId());
    }

    @Override
    @Transactional
    public void cancelServiceRequestNotifications(ServiceRequest serviceRequest) {
        // Clear the notified providers list except for the accepted provider
        ServiceProvider acceptedProvider = serviceRequest.getAcceptedProvider();
        serviceRequest.getNotifiedProviders().clear();

        // Add back only the accepted provider
        if (acceptedProvider != null) {
            serviceRequest.addNotifiedProvider(acceptedProvider);
        }

        serviceRequestRepository.save(serviceRequest);

        // Delete notifications for this service request
        notificationService.deleteNotificationsForServiceRequest(serviceRequest);

        logger.info("Cancelled notifications for service request: {}", serviceRequest.getId());
    }

    // Helper methods
    private AvailabilityResponse convertToAvailabilityResponse(Availability availability) {
        return new AvailabilityResponse(
                availability.getId(),
                availability.getDayOfWeek(),
                availability.getStartTime(),
                availability.getEndTime()
        );
    }

    private TimeSlotResponse convertToTimeSlotResponse(TimeSlot timeSlot) {
        Long serviceRequestId = null;
        if (timeSlot.getServiceRequest() != null) {
            serviceRequestId = timeSlot.getServiceRequest().getId();
        }

        return new TimeSlotResponse(
                timeSlot.getId(),
                timeSlot.getStartTime(),
                timeSlot.getEndTime(),
                serviceRequestId
        );
    }
}
