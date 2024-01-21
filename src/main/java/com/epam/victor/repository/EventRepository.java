package com.epam.victor.repository;

import com.epam.victor.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    List<Event> findAllByDateBetween(Instant dateStart, Instant dateEnd);
}
