package org.example.payload.response;

import java.time.LocalDateTime;

public class NotificationResponse {

    private Long id;
    private String message;
    private LocalDateTime createdAt;
    private boolean isRead;
    private Long serviceRequestId;
    private String serviceRequestCategory;
    private String serviceRequestLocation;
    private LocalDateTime serviceRequestPreferredDate;

    public NotificationResponse() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Long getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(Long serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public String getServiceRequestCategory() {
        return serviceRequestCategory;
    }

    public void setServiceRequestCategory(String serviceRequestCategory) {
        this.serviceRequestCategory = serviceRequestCategory;
    }

    public String getServiceRequestLocation() {
        return serviceRequestLocation;
    }

    public void setServiceRequestLocation(String serviceRequestLocation) {
        this.serviceRequestLocation = serviceRequestLocation;
    }

    public LocalDateTime getServiceRequestPreferredDate() {
        return serviceRequestPreferredDate;
    }

    public void setServiceRequestPreferredDate(LocalDateTime serviceRequestPreferredDate) {
        this.serviceRequestPreferredDate = serviceRequestPreferredDate;
    }
}
