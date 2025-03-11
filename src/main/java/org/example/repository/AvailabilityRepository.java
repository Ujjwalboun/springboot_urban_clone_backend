package org.example.repository;

import org.example.model.Availability;
import org.example.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByServiceProvider(ServiceProvider serviceProvider);
    Optional<Availability> findByServiceProviderAndDayOfWeek(ServiceProvider serviceProvider, DayOfWeek dayOfWeek);
}
