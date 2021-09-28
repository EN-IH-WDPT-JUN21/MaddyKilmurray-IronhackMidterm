package com.ironhack.midterm.dao.users.usersubclasses;

import com.ironhack.midterm.dao.users.Role;
import com.ironhack.midterm.dao.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    public Admin(String name, String username, String password) {
        super(name, username, password);
        addRole(new Role("ADMIN",this));
    }
}
