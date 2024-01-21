package com.epam.victor.repository;

import com.epam.victor.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findUserAccountByUser_Id(Long userId);
    void deleteUserAccountByUser_Id(Long userId);
}
