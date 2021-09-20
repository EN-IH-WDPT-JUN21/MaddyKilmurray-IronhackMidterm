package com.ironhack.midterm.dao.users.usersubclasses;

import com.ironhack.midterm.dao.Address;
import com.ironhack.midterm.dao.users.Role;
import com.ironhack.midterm.dao.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@DiscriminatorValue("THIRDPARTY")
public class ThirdParty extends User {

    @NotNull
    @Column(name = "hashed_key")
    private String hashedKey;

    @Transient
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ThirdParty(String name, String username, String password, Set<Role> roles, String hashedKey) {
        super(name, username, password, roles);
        this.hashedKey = encoder.encode(hashedKey);
    }
}
