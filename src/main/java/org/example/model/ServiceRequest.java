package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "service_requests")
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String category;

    @NotBlank
    private String description;

    @NotBlank
    private String location;

    @NotNull
    private LocalDateTime preferredDate;

    @Enumerated(EnumType.STRING)
    private ServiceRequestStatus status = ServiceRequestStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToMany
    @JoinTable(
            name = "service_request_notifications",
            joinColumns = @JoinColumn(name = "service_request_id"),
            inverseJoinColumns = @JoinColumn(name = "service_provider_id")
    )
    private Set<ServiceProvider> notifiedProviders = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "accepted_provider_id")
    private ServiceProvider acceptedProvider;

    private LocalDateTime createdAt = LocalDateTime.now();

    public ServiceRequest() {
    }

    public ServiceRequest(String category, String description, String location,
                          LocalDateTime preferredDate, Customer customer) {
        this.category = category;
        this.description = description;
        this.location = location;
        this.preferredDate = preferredDate;
        this.customer = customer;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public ServiceRequestStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceRequestStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<ServiceProvider> getNotifiedProviders() {
        return notifiedProviders;
    }

    public void setNotifiedProviders(Set<ServiceProvider> notifiedProviders) {
        this.notifiedProviders = notifiedProviders;
    }

    public ServiceProvider getAcceptedProvider() {
        return acceptedProvider;
    }

    public void setAcceptedProvider(ServiceProvider acceptedProvider) {
        this.acceptedProvider = acceptedProvider;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public void addNotifiedProvider(ServiceProvider provider) {
        this.notifiedProviders.add(provider);
    }
}
