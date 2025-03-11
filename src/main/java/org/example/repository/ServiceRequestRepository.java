package org.example.repository;

import org.example.model.Customer;
import org.example.model.ServiceProvider;
import org.example.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByCustomer(Customer customer);
    List<ServiceRequest> findByNotifiedProvidersContaining(ServiceProvider serviceProvider);
    List<ServiceRequest> findByAcceptedProvider(ServiceProvider serviceProvider);
    List<ServiceRequest> findByCategory(String category);
}
