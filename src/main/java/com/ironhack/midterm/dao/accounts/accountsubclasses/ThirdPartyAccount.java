package com.ironhack.midterm.dao.accounts.accountsubclasses;

import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class ThirdPartyAccount extends Account {

    private static final String accountType = "Third Party Account";

    public ThirdPartyAccount(long id, @NotNull Money balance, @NotBlank AccountHolder primaryOwner, AccountHolder secondaryOwner, @NotNull Money penaltyFee, @NotNull LocalDate creationDate, @NotNull Status status) {
        super(id, balance, primaryOwner, secondaryOwner, penaltyFee, creationDate, status);
    }
}
