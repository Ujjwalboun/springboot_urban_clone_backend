package org.example.service;

import org.example.model.ServiceProvider;
import org.example.model.ServiceRequest;
import org.example.payload.request.AvailabilityRequest;
import org.example.payload.response.AvailabilityResponse;
import org.example.payload.response.TimeSlotResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarService {
    AvailabilityResponse updateAvailability(ServiceProvider serviceProvider, AvailabilityRequest request);
    List<AvailabilityResponse> getAvailabilityForServiceProvider(ServiceProvider serviceProvider);
    List<TimeSlotResponse> getTimeSlotsByServiceProvider(ServiceProvider serviceProvider);
    boolean isServiceProviderAvailable(ServiceProvider serviceProvider, LocalDateTime startTime, LocalDateTime endTime);
    void blockTimeSlot(ServiceProvider serviceProvider, ServiceRequest serviceRequest, LocalDateTime startTime, LocalDateTime endTime);
    void cancelServiceRequestNotifications(ServiceRequest serviceRequest);
}