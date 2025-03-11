package org.example.service.impl;

import org.example.logging.AuditLogger;
import org.example.model.Customer;
import org.example.model.ServiceProvider;
import org.example.payload.request.CustomerRegistrationRequest;
import org.example.payload.request.LoginRequest;
import org.example.payload.request.ServiceProviderRegistrationRequest;
import org.example.payload.response.JwtResponse;
import org.example.payload.response.MessageResponse;
import org.example.repository.CustomerRepository;
import org.example.repository.ServiceProviderRepository;
import org.example.repository.UserRepository;
import org.example.security.jwt.JwtUtils;
import org.example.security.services.UserDetailsImpl;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuditLogger auditLogger;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Log user login
        auditLogger.logUserLogin(userDetails.getEmail());

        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getEmail(),
                userDetails.getPhone(),
                roles);
    }

    @Override
    public MessageResponse registerCustomer(CustomerRegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        if (userRepository.existsByPhone(registrationRequest.getPhone())) {
            return new MessageResponse("Error: Phone number is already in use!");
        }

        // Create new customer
        Customer customer = new Customer(
                registrationRequest.getName(),
                registrationRequest.getEmail(),
                encoder.encode(registrationRequest.getPassword()),
                registrationRequest.getPhone(),
                registrationRequest.getAddress());

        customerRepository.save(customer);

        // Log customer registration
        auditLogger.logUserRegistration("Customer", registrationRequest.getEmail());

        return new MessageResponse("Customer registered successfully!");
    }

    @Override
    public MessageResponse registerServiceProvider(ServiceProviderRegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        if (userRepository.existsByPhone(registrationRequest.getPhone())) {
            return new MessageResponse("Error: Phone number is already in use!");
        }

        // Create new service provider
        ServiceProvider serviceProvider = new ServiceProvider(
                registrationRequest.getName(),
                registrationRequest.getEmail(),
                encoder.encode(registrationRequest.getPassword()),
                registrationRequest.getPhone(),
                registrationRequest.getServiceType(),
                registrationRequest.getDescription(),
                registrationRequest.getHourlyRate());

        serviceProviderRepository.save(serviceProvider);

        // Log service provider registration
        auditLogger.logUserRegistration("ServiceProvider", registrationRequest.getEmail());

        return new MessageResponse("Service Provider registered successfully!");
    }
}
