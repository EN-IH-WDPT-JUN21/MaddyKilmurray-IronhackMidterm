package com.ironhack.midterm.dao.users.usersubclasses;

import com.ironhack.midterm.dao.users.Role;
import com.ironhack.midterm.dao.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    public Admin(String name, String username, String password, Set<Role> roles) {
        super(name, username, password, roles);
    }
}
