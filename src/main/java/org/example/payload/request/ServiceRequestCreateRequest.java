package org.example.payload.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ServiceRequestCreateRequest {

    @NotBlank
    private String category;

    @NotBlank
    private String description;

    @NotBlank
    private String location;

    @NotNull
    @Future
    private LocalDateTime preferredDate;

    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(LocalDateTime preferredDate) {
        this.preferredDate = preferredDate;
    }
}
