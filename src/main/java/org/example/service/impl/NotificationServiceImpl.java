package org.example.service.impl;

import org.example.model.Notification;
import org.example.model.ServiceProvider;
import org.example.model.ServiceRequest;
import org.example.payload.response.NotificationCountResponse;
import org.example.payload.response.NotificationResponse;
import org.example.repository.NotificationRepository;
import org.example.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void notifyServiceProviders(ServiceRequest serviceRequest, List<ServiceProvider> serviceProviders) {
        // In a real application, this would send emails, SMS, or push notifications
        // For simplicity, we'll just create notification records in the database

        for (ServiceProvider provider : serviceProviders) {
            logger.info("Notifying service provider: {} about new service request: {} in category: {}",
                    provider.getName(), serviceRequest.getId(), serviceRequest.getCategory());

            // Add the provider to the notified providers list
            serviceRequest.addNotifiedProvider(provider);

            // Create a notification record
            String message = String.format("New service request in %s category at %s",
                    serviceRequest.getCategory(), serviceRequest.getLocation());

            Notification notification = new Notification(provider, serviceRequest, message);
            notificationRepository.save(notification);
        }
    }

    @Override
    public List<NotificationResponse> getNotificationsForServiceProvider(ServiceProvider serviceProvider) {
        List<Notification> notifications = notificationRepository.findByServiceProviderOrderByCreatedAtDesc(serviceProvider);

        return notifications.stream()
                .map(this::convertToNotificationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponse> getUnreadNotificationsForServiceProvider(ServiceProvider serviceProvider) {
        List<Notification> notifications = notificationRepository.findByServiceProviderAndIsReadOrderByCreatedAtDesc(
                serviceProvider, false);

        return notifications.stream()
                .map(this::convertToNotificationResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NotificationResponse markNotificationAsRead(Long notificationId, ServiceProvider serviceProvider) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId));

        // Check if the notification belongs to the service provider
        if (!notification.getServiceProvider().getId().equals(serviceProvider.getId())) {
            throw new IllegalStateException("Notification does not belong to this service provider");
        }

        notification.setRead(true);
        notification = notificationRepository.save(notification);

        return convertToNotificationResponse(notification);
    }

    @Override
    @Transactional
    public void markAllNotificationsAsRead(ServiceProvider serviceProvider) {
        List<Notification> unreadNotifications = notificationRepository.findByServiceProviderAndIsReadOrderByCreatedAtDesc(
                serviceProvider, false);

        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    public NotificationCountResponse getNotificationCount(ServiceProvider serviceProvider) {
        long unreadCount = notificationRepository.countByServiceProviderAndIsRead(serviceProvider, false);
        long totalCount = notificationRepository.findByServiceProviderOrderByCreatedAtDesc(serviceProvider).size();

        return new NotificationCountResponse(unreadCount, totalCount);
    }

    @Override
    @Transactional
    public void deleteNotificationsForServiceRequest(ServiceRequest serviceRequest) {
        List<Notification> notifications = notificationRepository.findByServiceRequest(serviceRequest);

        notificationRepository.deleteAll(notifications);
    }

    // Helper method to convert Notification to NotificationResponse
    private NotificationResponse convertToNotificationResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setMessage(notification.getMessage());
        response.setCreatedAt(notification.getCreatedAt());
        response.setRead(notification.isRead());

        if (notification.getServiceRequest() != null) {
            ServiceRequest serviceRequest = notification.getServiceRequest();
            response.setServiceRequestId(serviceRequest.getId());
            response.setServiceRequestCategory(serviceRequest.getCategory());
            response.setServiceRequestLocation(serviceRequest.getLocation());
            response.setServiceRequestPreferredDate(serviceRequest.getPreferredDate());
        }

        return response;
    }
}
