package com.epam.victor.service.impl;

import com.epam.victor.model.Event;
import com.epam.victor.model.Ticket;
import com.epam.victor.model.User;
import com.epam.victor.model.UserAccount;
import com.epam.victor.repository.TicketRepository;
import com.epam.victor.repository.UserAccountRepository;
import com.epam.victor.service.EventService;
import com.epam.victor.service.TicketService;
import com.epam.victor.service.UserService;
import com.epam.victor.service.exception.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private final UserService userService;

    private final EventService eventService;

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository,
                             UserService userService,
                             EventService eventService,
                             UserAccountRepository userAccountRepository) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    @Transactional
    public Ticket book(long userId, long eventId, int place, Ticket.Category category, BigDecimal price) {
        User user = userService.getById(userId);
        UserAccount userAccount = user.getUserAccount();
        userAccount.withdraw(price);
        userAccountRepository.save(userAccount);
        Event event = eventService.getById(eventId);
        return ticketRepository.save(new Ticket(event, user, category, place, price));
    }

    @Override
    public List<Ticket> getBooked(User user, int pageSize, int pageNum) {
        return ticketRepository.findAllByUser(user, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public List<Ticket> getBooked(Event event, int pageSize, int pageNum) {
        return ticketRepository.findAllByEvent(event, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public boolean cancel(long ticketId) {
        ticketRepository.deleteById(ticketId);
        return true;
    }

    @Override
    public Ticket getById(long id) {
        return ticketRepository.findById(id).orElseThrow(
                () -> new IdNotFoundException("Ticket with id " + id + "not found"));
    }
}
