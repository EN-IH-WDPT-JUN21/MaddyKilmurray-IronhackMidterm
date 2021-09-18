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

@Getter
@Setter
@Entity
public class ThirdParty extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    @Column(name = "hashed_key")
    private String hashedKey;

    @Transient
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public Long getId() {
        return this.id;
    }

    public ThirdParty(String username, String password, Role role, String name, String hashedKey) {
        super(username, password, new HashSet<>(Arrays.asList(role)));
        this.name = name;
        this.hashedKey = encoder.encode(hashedKey);
    }
}
