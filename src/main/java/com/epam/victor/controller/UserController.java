package com.epam.victor.controller;

import com.epam.victor.controller.exception.EmailIsEmptyException;
import com.epam.victor.facade.BookingFacade;
import com.epam.victor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final BookingFacade bookingFacade;

    @Autowired
    public UserController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping
    public String showUsersByName(@RequestParam(value = "name", required = false) String name, Model model){
        List<User> users = new ArrayList<>();
        if (name != null && !name.isBlank()) {
            users = bookingFacade.getUsersByName(name, 10, 0);
        }
        model.addAttribute("users", users);
        model.addAttribute("current_name", name);
        return "users/users_by_name";
    }

    @GetMapping("/search")
    public String showUsersByEmail(Model model, @RequestParam(value = "email", required = false) String email){
        User user = null;
        if (email != null && !email.isBlank()) {
            user = bookingFacade.getUserByEmail(email);
        } else {
            email = "";
        }
        model.addAttribute("user", user);
        model.addAttribute("current_email", email);
        return "users/users_by_email";
    }


    @GetMapping("/{id}")
    public String showUserById(@PathVariable("id") Long id, Model model){
        User user = bookingFacade.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("tickets", bookingFacade.getBookedTickets(user, 15, 0));
        return "users/user_page";
    }

    @GetMapping("/{id}/update")
    public String showUpdatePage(@PathVariable("id") Long id, Model model){
        User User = bookingFacade.getUserById(id);
        model.addAttribute("user", User);
        return "users/user_update_page";
    }

    @GetMapping("/new")
    public String showCreatePage(@ModelAttribute("user") User user){
        return "users/user_create_page";
    }

    @PostMapping
    public String createNewUser(@ModelAttribute("user") User user){
        Long id = bookingFacade.createUser(user).getId();
        return "redirect:/users/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        bookingFacade.deleteUser(id);
        return "redirect:/users";
    }

    @PatchMapping("/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user){
        bookingFacade.updateUser(user);
        return "redirect:/users/" + id;
    }
}
