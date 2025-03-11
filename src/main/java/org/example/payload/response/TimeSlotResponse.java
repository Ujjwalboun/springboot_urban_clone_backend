package org.example.payload.response;

import java.time.LocalDateTime;

public class TimeSlotResponse {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long serviceRequestId;

    public TimeSlotResponse() {
    }

    public TimeSlotResponse(Long id, LocalDateTime startTime, LocalDateTime endTime, Long serviceRequestId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.serviceRequestId = serviceRequestId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(Long serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }
}
