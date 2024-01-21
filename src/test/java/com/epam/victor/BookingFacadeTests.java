package com.epam.victor;

import com.epam.victor.facade.BookingFacade;
import com.epam.victor.model.Event;
import com.epam.victor.model.Ticket;
import com.epam.victor.model.User;
import com.epam.victor.model.UserAccount;
import com.epam.victor.repository.UserRepository;
import com.epam.victor.service.exception.IdNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingFacadeTests {

	@Autowired
	BookingFacade bookingFacade;

	@Autowired
	UserRepository userRepository;


	private List<User> userCollection = List.of(
			new User("User1","user1@mail.com"),
			new User("User2","user2@mail.com"),
			new User("User3","user3@mail.com"),
			new User("User3","user4@mail.com")
	);

	private List<Event> eventCollection = List.of(
			new Event("Event1", Instant.parse("2024-01-01T00:00:00Z")),
			new Event("Event2", Instant.parse("2024-01-02T00:00:00Z")),
			new Event("Event3", Instant.parse("2024-01-01T00:00:00Z")),
			new Event("Event4", Instant.parse("2024-01-02T00:00:00Z")),
			new Event("Event4", Instant.parse("2024-01-03T00:00:00Z"))
	);

	private List<Ticket> ticketCollection;



	@BeforeEach
	void initDb(){
		initEvents();
		initUsers();
		ticketCollection = List.of(
				new Ticket(bookingFacade.getEventById(1L), bookingFacade.getUserById(1L), Ticket.Category.PREMIUM, 11, BigDecimal.valueOf(0.0)),
				new Ticket( bookingFacade.getEventById(2L), bookingFacade.getUserById(1L), Ticket.Category.BAR, 22, BigDecimal.valueOf(0.0)),
				new Ticket( bookingFacade.getEventById(2L), bookingFacade.getUserById(3L), Ticket.Category.BAR, 22, BigDecimal.valueOf(0.0)),
				new Ticket( bookingFacade.getEventById(2L), bookingFacade.getUserById(1L), Ticket.Category.BAR, 23, BigDecimal.valueOf(0.0))
		);
		initTickets();
	}

	private void initEvents(){
		eventCollection.forEach(e -> bookingFacade.createEvent(e));
	}

	private void initUsers(){
		for(User user : userCollection){
			user.setUserAccount(new UserAccount(BigDecimal.valueOf(100)));
			bookingFacade.createUser(user);
		}
	}

	private void initTickets(){
		for (Ticket ticket : ticketCollection){
			bookingFacade.bookTicket(ticket.getUser().getId(),
					ticket.getEvent().getId(),
					ticket.getPlace(),
					ticket.getCategory(),
					ticket.getPrice());
		}
		for (int i = 1; i < ticketCollection.size() + 1; i++){
			ticketCollection.get(i - 1).setId((long) i);
		}

	}

	@Test
	void contextShouldBeUp(){
	}



	@Test
	void eventShouldBeGotById(){
		Event expected = eventCollection.getFirst();
		Event actual = bookingFacade.getEventById(1L);
		assertEquals(expected, actual);
	}

	@Test
	void notExistingEventIdShouldThrowException(){
		assertThrows(IdNotFoundException.class, () -> bookingFacade.getEventById(6L));
	}

	@Test
	void allEventsOfDayShouldBeTaken(){
		assertEquals(
				List.of(eventCollection.get(1), eventCollection.get(3)),
				bookingFacade.getEventsForDay(LocalDate.of(2024,1,2),5,0));
	}

	@Test
	void allEventsOfTitleShouldBeTaken(){
		String title = "Event4";
		List<Event> expected = eventCollection.stream().filter(e -> e.getTitle().equals(title)).toList();
		List<Event> actual = bookingFacade.getEventsByTitle(title,5,0);
		assertEquals(expected, actual);
	}

	@Test
	void eventShouldBeCreated() {
		Event newEvent = new Event("Event6", Instant.parse("2024-01-11T12:22:00Z"));
		bookingFacade.createEvent(newEvent);
		newEvent.setId(6L);
		Event actual = bookingFacade.getEventById(6L);
		assertEquals(newEvent, actual);
	}

	@Test
	void eventShouldBeUpdated() {
		Event current = bookingFacade.getEventById(5L);
		Event updated = new Event( current.getTitle() + "_updated",current.getDate());
		updated.setId(current.getId());
		bookingFacade.updateEvent(updated);
		assertEquals(updated, bookingFacade.getEventById(5L));
	}

	@Test
	void eventShouldBeDeleted (){
		Long eventId = 4L;
		assertDoesNotThrow(() -> bookingFacade.getEventById(eventId));
		boolean deleted = bookingFacade.deleteEvent(eventId);
		assertTrue(deleted);
		assertThrows(
				IdNotFoundException.class,
				() -> bookingFacade.getEventById(eventId));
	}


	@Test
	void userShouldBeGetById(){
		User expected = userCollection.getFirst();
		User actual = bookingFacade.getUserById(1L);
		assertEquals(expected, actual);
	}

	@Test
	void notExistingUserIdShouldThrowException(){
		assertThrows(IdNotFoundException.class, () -> bookingFacade.getUserById(5L));
	}

	@Test
	void userShouldBeCreated() {
		User newUser = new User("User5","user5@mail.com");
		bookingFacade.createUser(newUser);
		User actual = bookingFacade.getUserById(5L);
		newUser.setId(5L);
		newUser.setUserAccount(new UserAccount(BigDecimal.valueOf(0.00)));
		newUser.getUserAccount().setId(5L);
		assertEquals(newUser, actual);
	}

	@Test
	void userShouldBeUpdated() {
		User current = bookingFacade.getUserById(4L);
		User updated = new User( current.getName() + "_updated", current.getEmail());
		updated.setId(4L);
		updated.setUserAccount(current.getUserAccount());
		bookingFacade.updateUser(updated);
		assertEquals(updated, bookingFacade.getUserById(4L));
	}

	@Test
	void userShouldBeDeleted (){
		Long userId = 4L;
		assertDoesNotThrow(() -> bookingFacade.getUserById(userId));
		boolean deleted = bookingFacade.deleteUser(userId);
		assertTrue(deleted);
		assertThrows(IdNotFoundException.class, () -> bookingFacade.getUserById(userId));
	}

	@Test
	void userShouldBeGotByEmail(){
		User expected = userCollection.getFirst();
		User actual = bookingFacade.getUserByEmail(expected.getEmail());
		assertEquals(expected, actual);
	}

	@Test
	void usersShouldBeGotByName(){
		String userName = "User3";
		List<User> expected = userCollection.stream().filter(u -> u.getName().equals("User3")).toList();
		List<User> actual = bookingFacade.getUsersByName(userName,5,0);
		assertEquals(expected, actual);
	}

	@Test
	void ticketShouldBeGotById (){
		Ticket expected = ticketCollection.getFirst();
		Ticket actual = bookingFacade.getTicketById(1L);
		assertEquals(expected, actual);
	}

	@Test
	void notExistingIdShouldThrowException(){
		assertThrows(IdNotFoundException.class, () -> bookingFacade.getTicketById(5L));
	}
	@Test
	void ticketShouldBeBooked(){
		Long userId = 1L;
		Long eventId = 2L;
		Integer place = 23;
		BigDecimal initialBalance = bookingFacade.getUserById(userId).getUserAccount().getAmount();
		BigDecimal price = BigDecimal.valueOf(15);
		Ticket.Category category = Ticket.Category.BAR;
		Ticket newTicket = bookingFacade.bookTicket(userId, eventId, place, category, price);
		Ticket expected = new Ticket(bookingFacade.getEventById(eventId),
				bookingFacade.getUserById(userId),
				category,
				place,
				price);
		expected.setId(5L);
		assertEquals(expected, newTicket);
        assertEquals(0, bookingFacade.getUserById(userId).getUserAccount().getAmount().compareTo(initialBalance.subtract(price)));
		assertEquals(bookingFacade.getTicketById(newTicket.getId()), newTicket);
	}

	@Test
	void bookedByUserTicketsShouldBeTaken(){
		Long userId = 1L;
		List<Ticket> actual = bookingFacade.getBookedTickets(bookingFacade.getUserById(userId),5,0);
		List<Ticket> expected = ticketCollection.stream().filter(t -> t.getUser().getId().equals(userId)).toList();
		assertEquals(expected, actual);
	}

	@Test
	void bookedTicketsOfEventShouldBeTaken(){
		Long eventId = 2L;
		List<Ticket> actual = bookingFacade.getBookedTickets(bookingFacade.getEventById(eventId),5,0);
		List<Ticket> expected = ticketCollection.stream().filter(t -> t.getEvent().getId().equals(eventId)).toList();
		assertEquals(expected, actual);
	}

	@Test
	void ticketShouldBeCanceled (){
		Long ticketId = 4L;
		assertDoesNotThrow(() -> bookingFacade.getTicketById(ticketId));
		boolean deleted = bookingFacade.cancelTicket(ticketId);
		assertTrue(deleted);
		assertThrows(IdNotFoundException.class, () -> bookingFacade.getTicketById(ticketId));
	}

	@Test
	void moneyShouldBeTransferred(){
		BigDecimal amount = BigDecimal.valueOf(150);
		User user = bookingFacade.getUserById(1L);
		BigDecimal currentBalance = user.getUserAccount().getAmount();
		BigDecimal expected = currentBalance.add(amount);
		bookingFacade.depositMoney(1L, amount);
		assertEquals(expected, bookingFacade.getUserById(1L).getUserAccount().getAmount());
	}


}
