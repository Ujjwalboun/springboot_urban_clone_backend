package org.example.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AuditLogger {

    private static final Logger logger = LoggerFactory.getLogger("AuditLogger");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logUserRegistration(String userType, String email) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] User Registration - Type: {}, Email: {}", timestamp, userType, email);
    }

    public void logUserLogin(String email) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] User Login - Email: {}", timestamp, email);
    }

    public void logServiceRequestCreation(Long requestId, String category, String customerEmail) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] Service Request Created - ID: {}, Category: {}, Customer: {}",
                timestamp, requestId, category, customerEmail);
    }

    public void logServiceRequestAcceptance(Long requestId, String providerEmail) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] Service Request Accepted - ID: {}, Provider: {}",
                timestamp, requestId, providerEmail);
    }

    public void logAvailabilityUpdate(String providerEmail, String dayOfWeek, String timeRange) {
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] Availability Updated - Provider: {}, Day: {}, Time Range: {}",
                timestamp, providerEmail, dayOfWeek, timeRange);
    }
}