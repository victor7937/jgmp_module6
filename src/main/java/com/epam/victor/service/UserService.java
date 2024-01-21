package com.epam.victor.service;

import com.epam.victor.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    User getById(Long id);

    User getByEmail(String email);

    List<User> getByName(String name, int pageSize, int pageNum);

    User create(User user);

    User update(User user);

    boolean delete(long userId);

    void depositMoney(long userId, BigDecimal value);
}
