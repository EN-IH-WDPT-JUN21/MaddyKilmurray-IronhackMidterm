package com.ironhack.midterm.controller.dto.users;

import com.ironhack.midterm.dao.Address;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountHolderDTO {

    private long id;

    @NotNull
    private String name;

    private String username;
    private String password;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private Address primaryAddress;

    private Address mailingAddress;

    private List<Account> accounts;

    public AccountHolderDTO(String name, String username, String password, LocalDate dateOfBirth, Address primaryAddress, List<Account> accounts) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.accounts = accounts;
    }

    public AccountHolderDTO(String name, String username, String password, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress, List<Account> accounts) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
        this.accounts = accounts;
    }
}
