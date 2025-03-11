package org.example.repository;

import org.example.model.Notification;
import org.example.model.ServiceProvider;
import org.example.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByServiceProviderOrderByCreatedAtDesc(ServiceProvider serviceProvider);
    List<Notification> findByServiceProviderAndIsReadOrderByCreatedAtDesc(ServiceProvider serviceProvider, boolean isRead);
    List<Notification> findByServiceRequest(ServiceRequest serviceRequest);
    long countByServiceProviderAndIsRead(ServiceProvider serviceProvider, boolean isRead);
}
