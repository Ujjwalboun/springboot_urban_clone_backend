package org.example.service;

import org.example.payload.request.CustomerRegistrationRequest;
import org.example.payload.request.LoginRequest;
import org.example.payload.request.ServiceProviderRegistrationRequest;
import org.example.payload.response.JwtResponse;
import org.example.payload.response.MessageResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    MessageResponse registerCustomer(CustomerRegistrationRequest registrationRequest);
    MessageResponse registerServiceProvider(ServiceProviderRegistrationRequest registrationRequest);
}
