package com.epam.victor.controller;

import com.epam.victor.controller.util.DateFormatUtil;
import com.epam.victor.facade.BookingFacade;
import com.epam.victor.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    private final BookingFacade bookingFacade;

    @Autowired
    public EventController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping
    public String showEventsByDate(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model){
        if (date == null) {
            date = LocalDate.ofInstant(Instant.now(), ZoneOffset.UTC);
        }
        model.addAttribute("current_date", date);
        model.addAttribute("events", bookingFacade.getEventsForDay(date,10, 0));
        return "events/events_by_date";
    }

    @GetMapping("/search")
    public String showEventsByTitle(Model model, @RequestParam(value = "title", required = false) String title){
        List<Event> events = new ArrayList<>();
        if (title != null && !title.isBlank()) {
            events = bookingFacade.getEventsByTitle(title, 10, 0);
        }
        model.addAttribute("events", events);
        model.addAttribute("title", title);
        return "events/events_by_title";
    }


    @GetMapping("/{id}")
    public String showEventById(@PathVariable("id") Long id, Model model){
        model.addAttribute("event", bookingFacade.getEventById(id));
        return "events/event_page";
    }

    @GetMapping("/{id}/update")
    public String showUpdatePage(@PathVariable("id") Long id, Model model){
        Event event = bookingFacade.getEventById(id);
        model.addAttribute("event", event);
        model.addAttribute("event_timestamp", LocalDateTime.ofInstant(event.getDate(), ZoneOffset.UTC));
        return "events/event_update_page";
    }

    @GetMapping("/new")
    public String showCreatePage(){
        return "events/event_create_page";
    }

    @PostMapping
    public String createNewEvent(@RequestParam("title") String title,
                                 @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
        Event event = new Event(title, date.toInstant(ZoneOffset.UTC));
        Long id = bookingFacade.createEvent(event).getId();
        return "redirect:/events/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable("id") Long id){
        bookingFacade.deleteEvent(id);
        return "redirect:/events";
    }

    @PostMapping("/{id}/edit")
    public String updateEvent(@PathVariable("id") Long id,
                              @RequestParam("title") String title,
                              @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
    ){
        Event event = new Event(title, date.toInstant(ZoneOffset.UTC));
        event.setId(id);
        bookingFacade.updateEvent(event);
        return "redirect:/events/" + id;
    }


}
