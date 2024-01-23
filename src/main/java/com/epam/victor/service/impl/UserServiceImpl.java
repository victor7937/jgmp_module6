package com.epam.victor.service.impl;

import com.epam.victor.model.User;
import com.epam.victor.model.UserAccount;
import com.epam.victor.repository.UserAccountRepository;
import com.epam.victor.repository.UserRepository;
import com.epam.victor.service.UserService;
import com.epam.victor.service.exception.AccountNotFoundException;
import com.epam.victor.service.exception.EmailNotFoundException;
import com.epam.victor.service.exception.IdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserAccountRepository userAccountRepository) {
        this.userRepository = userRepository;
        this.userAccountRepository = userAccountRepository;
    }


    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new IdNotFoundException("User with id " + id + "not found"));
    }

    @Override
    public User getByEmail(String email) {
//        return userRepository.findByEmail(email).orElseThrow(
//                () -> new EmailNotFoundException("User " + email + " not found"));
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<User> getByName(String name, int pageSize, int pageNum) {
        return userRepository.findAllByNameContainingOrderByName(name, PageRequest.of(pageNum, pageSize));
    }

    @Override
    @Transactional
    public User create(User user) {
        UserAccount userAccount;
        if (user.getUserAccount() == null) {
            userAccount = new UserAccount(BigDecimal.ZERO);
        } else {
            userAccount = user.getUserAccount();
        }
        user = userRepository.save(user);
        userAccount.setUser(user);
        userAccountRepository.save(userAccount);
        return user;
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean delete(long userId) {
        userAccountRepository.deleteUserAccountByUser_Id(userId);
        userRepository.deleteById(userId);
        return true;
    }

    @Override
    @Transactional
    public void depositMoney(long userId, BigDecimal value) {
        UserAccount userAccount = userAccountRepository.findUserAccountByUser_Id(userId)
                .orElseThrow(() -> new AccountNotFoundException("Account for user id " + userId + " not found"));
        userAccount.deposit(value);
        userAccountRepository.save(userAccount);
    }
}
