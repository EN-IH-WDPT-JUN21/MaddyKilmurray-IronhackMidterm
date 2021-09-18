package com.ironhack.midterm.dao.users.usersubclasses;

import com.ironhack.midterm.dao.users.Role;
import com.ironhack.midterm.dao.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;

@Getter
@Setter
@Entity
public class Admin extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @Override
    public Long getId() {
        return this.id;
    }

    public Admin(String username, String password, Role role, String name) {
        super(username, password, new HashSet<>(Arrays.asList(role)));
        this.name = name;
    }
}
