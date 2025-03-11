package org.example.service;

import org.example.model.Customer;
import org.example.model.ServiceProvider;
import org.example.payload.request.ServiceRequestCreateRequest;
import org.example.payload.response.ServiceRequestResponse;

import java.util.List;

public interface ServiceRequestService {
    ServiceRequestResponse createServiceRequest(ServiceRequestCreateRequest request, Customer customer);
    ServiceRequestResponse getServiceRequestById(Long id);
    List<ServiceRequestResponse> getServiceRequestsByCustomer(Customer customer);
    List<ServiceRequestResponse> getServiceRequestsForProvider(ServiceProvider serviceProvider);
    List<ServiceRequestResponse> getServiceRequestsNotifiedToProvider(ServiceProvider serviceProvider);
    ServiceRequestResponse acceptServiceRequest(Long requestId, ServiceProvider serviceProvider);
}
