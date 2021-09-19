package com.ironhack.midterm.dao.users.usersubclasses;

import com.ironhack.midterm.dao.Address;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.Role;
import com.ironhack.midterm.dao.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    @AttributeOverrides({
            @AttributeOverride( name = "houseNumber", column = @Column(name = "primary_house_number")),
            @AttributeOverride( name = "firstLineOfAddress", column = @Column(name = "primary_first_line_of_address")),
            @AttributeOverride( name = "secondLineOfAddress", column = @Column(name = "primary_second_line_of_address")),
            @AttributeOverride( name = "thirdLineOfAddress", column = @Column(name = "primary_third_line_of_address")),
            @AttributeOverride( name = "city", column = @Column(name = "primary_city")),
            @AttributeOverride( name = "county", column = @Column(name = "primary_county")),
            @AttributeOverride( name = "country", column = @Column(name = "primary_country")),
            @AttributeOverride( name = "postcode", column = @Column(name = "primary_postcode"))
    })
    private Address primaryAddress;

    @Column(name = "mailing_address")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "houseNumber", column = @Column(name = "mailing_house_number")),
            @AttributeOverride( name = "firstLineOfAddress", column = @Column(name = "mailing_first_line_of_address")),
            @AttributeOverride( name = "secondLineOfAddress", column = @Column(name = "mailing_second_line_of_address")),
            @AttributeOverride( name = "thirdLineOfAddress", column = @Column(name = "mailing_third_line_of_address")),
            @AttributeOverride( name = "city", column = @Column(name = "mailing_city")),
            @AttributeOverride( name = "county", column = @Column(name = "mailing_county")),
            @AttributeOverride( name = "country", column = @Column(name = "mailing_country")),
            @AttributeOverride( name = "postcode", column = @Column(name = "mailing_postcode"))
    })
    private Address mailingAddress;

    @OneToMany(
            mappedBy = "primaryOwner",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private List<Account> accounts;

    @Override
    public Long getId() {
        return this.id;
    }

    public AccountHolder(String username, String password, Role role, String name, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress, Account account) {
        super(username, password, new HashSet<>(Arrays.asList(role)));
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
        this.accounts = new ArrayList<>();
        this.accounts.add(account);
    }

    public Boolean isUnder24() {
        LocalDate today = LocalDate.now();
        LocalDate flagValue
                = today.minus(24, ChronoUnit.YEARS);
        return this.getDateOfBirth().isAfter(flagValue);
    }
}
