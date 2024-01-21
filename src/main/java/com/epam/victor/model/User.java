package com.epam.victor.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name ="booking_user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    String name;

    String email;

    @OneToOne(mappedBy = "user")
    UserAccount userAccount;

//    public void setUserAccount(UserAccount userAccount){
//        this.userAccount = userAccount;
//        userAccount.setUser(this);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && (userAccount.getAmount().compareTo(user.userAccount.getAmount()) == 0) && Objects.equals(userAccount.getId(), user.userAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, userAccount.getId(), userAccount.getAmount());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userAccount=" + userAccount.getAmount() +
                '}';
    }
}

