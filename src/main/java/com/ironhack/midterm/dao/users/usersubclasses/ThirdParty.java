package com.ironhack.midterm.dao.users.usersubclasses;

import com.ironhack.midterm.dao.Address;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.accounts.accountsubclasses.ThirdPartyAccount;
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
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("THIRDPARTY")
public class ThirdParty extends User {

    @NotNull
    @Column(name = "hashed_key")
    private String hashedKey;

    @OneToMany(
            mappedBy = "primaryOwner",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private List<ThirdPartyAccount> accounts;

    public ThirdParty(String name, String username, String password, String hashedKey) {
        super(name, username, password);
        this.hashedKey = hashedKey;
        addRole(new Role("THIRDPARTY",this));
    }
}
