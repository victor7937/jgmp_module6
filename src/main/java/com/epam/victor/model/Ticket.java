package com.epam.victor.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Objects;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@Entity
@Table(name ="booking_ticket")
public class Ticket {

    public enum Category {STANDARD, PREMIUM, BAR}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    Category category;

    Integer place;

    BigDecimal price = BigDecimal.ZERO;

    public Ticket(Event event, User user, Category category, Integer place, BigDecimal price) {
        this.event = event;
        this.user = user;
        this.category = category;
        this.place = place;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(event, ticket.event) && Objects.equals(user, ticket.user) && category == ticket.category && Objects.equals(place, ticket.place) && price.compareTo(ticket.price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event, user, category, place, price);
    }
}

