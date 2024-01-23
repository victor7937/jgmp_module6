package com.epam.victor.service.impl;

import com.epam.victor.model.Event;
import com.epam.victor.repository.EventRepository;
import com.epam.victor.service.EventService;
import com.epam.victor.service.exception.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }


    @Override
    public Event getById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new IdNotFoundException("Event with id " + id + "not found"));
    }

    @Override
    public List<Event> getByTitle(String title, int pageSize, int pageNum) {
        return eventRepository.findAllByTitleContainingIgnoreCaseOrderByDate(title, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public List<Event> getOfDate(LocalDate day, int pageSize, int pageNum) {
        Instant start = day.atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant end = start.plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.MILLIS);
        return eventRepository.findAllByDateBetweenOrderByDate(start, end);
    }

    @Override
    public Event create(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event update(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public boolean delete(long eventId) {
        eventRepository.deleteById(eventId);
        return true;
    }
}
