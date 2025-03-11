package org.example.repository;

import org.example.model.ServiceProvider;
import org.example.model.ServiceRequest;
import org.example.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByServiceProvider(ServiceProvider serviceProvider);
    List<TimeSlot> findByServiceRequest(ServiceRequest serviceRequest);

    @Query("SELECT t FROM TimeSlot t WHERE t.serviceProvider = :provider " +
            "AND ((t.startTime <= :end AND t.endTime >= :start))")
    List<TimeSlot> findOverlappingTimeSlots(
            @Param("provider") ServiceProvider provider,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
