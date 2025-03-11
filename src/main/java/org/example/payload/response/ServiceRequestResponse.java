package org.example.payload.response;

import org.example.model.ServiceRequestStatus;

import java.time.LocalDateTime;
import java.util.Set;

public class ServiceRequestResponse {

    private Long id;
    private String category;
    private String description;
    private String location;
    private LocalDateTime preferredDate;
    private ServiceRequestStatus status;
    private CustomerInfoResponse customer;
    private Set<ServiceProviderInfoResponse> notifiedProviders;
    private ServiceProviderInfoResponse acceptedProvider;
    private LocalDateTime createdAt;

    // Nested DTO for customer info
    public static class CustomerInfoResponse {
        private Long id;
        private String name;
        private String email;
        private String phone;

        public CustomerInfoResponse() {
        }

        public CustomerInfoResponse(Long id, String name, String email, String phone) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    // Nested DTO for service provider info
    public static class ServiceProviderInfoResponse {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String serviceType;

        public ServiceProviderInfoResponse() {
        }

        public ServiceProviderInfoResponse(Long id, String name, String email, String phone, String serviceType) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.serviceType = serviceType;
        }

        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }
    }

    // Constructors
    public ServiceRequestResponse() {
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

    public CustomerInfoResponse getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerInfoResponse customer) {
        this.customer = customer;
    }

    public Set<ServiceProviderInfoResponse> getNotifiedProviders() {
        return notifiedProviders;
    }

    public void setNotifiedProviders(Set<ServiceProviderInfoResponse> notifiedProviders) {
        this.notifiedProviders = notifiedProviders;
    }

    public ServiceProviderInfoResponse getAcceptedProvider() {
        return acceptedProvider;
    }

    public void setAcceptedProvider(ServiceProviderInfoResponse acceptedProvider) {
        this.acceptedProvider = acceptedProvider;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
