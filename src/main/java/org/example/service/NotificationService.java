package org.example.service;

import org.example.model.ServiceProvider;
import org.example.model.ServiceRequest;
import org.example.payload.response.NotificationCountResponse;
import org.example.payload.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    void notifyServiceProviders(ServiceRequest serviceRequest, List<ServiceProvider> serviceProviders);
    List<NotificationResponse> getNotificationsForServiceProvider(ServiceProvider serviceProvider);
    List<NotificationResponse> getUnreadNotificationsForServiceProvider(ServiceProvider serviceProvider);
    NotificationResponse markNotificationAsRead(Long notificationId, ServiceProvider serviceProvider);
    void markAllNotificationsAsRead(ServiceProvider serviceProvider);
    NotificationCountResponse getNotificationCount(ServiceProvider serviceProvider);
    void deleteNotificationsForServiceRequest(ServiceRequest serviceRequest);
}
