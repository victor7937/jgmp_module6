package com.epam.victor.model;

import com.epam.victor.model.exception.YouDontHaveMoneyException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@Entity
@Table(name ="user_account")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    BigDecimal amount;

    public UserAccount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setUser(User user) {
        this.user = user;
        user.setUserAccount(this);
    }

    public void withdraw (BigDecimal value){
        if (amount.compareTo(value) < 0){
            throw new YouDontHaveMoneyException("No ticket, go home");
        }
        amount = amount.subtract(value);
    }

    public void deposit (BigDecimal value){
        amount = amount.add(value);
    }
}
