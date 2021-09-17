package com.ironhack.midterm.dao.users;

import com.ironhack.midterm.dao.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Entity
public class AccountHolder extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    @Column(name = "primary_address")
    private Address primaryAddress;

    @Column(name = "mailing_address")
    @Embedded
    private Address mailingAddress;

    @Override
    public Long getId() {
        return this.id;
    }

    public AccountHolder(String username, String password, Role role, String name, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        super(username, password, new HashSet<>(Arrays.asList(role)));
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }
}
