package org.example.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "service_providers")
@DiscriminatorValue("SERVICE_PROVIDER")
public class ServiceProvider extends User {

    private String serviceType;
    private String description;
    private double hourlyRate;

    public ServiceProvider() {
        super();
    }

    public ServiceProvider(String name, String email, String password, String phone,
                           String serviceType, String description, double hourlyRate) {
        super(name, email, password, phone, Role.ROLE_SERVICE_PROVIDER);
        this.serviceType = serviceType;
        this.description = description;
        this.hourlyRate = hourlyRate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
