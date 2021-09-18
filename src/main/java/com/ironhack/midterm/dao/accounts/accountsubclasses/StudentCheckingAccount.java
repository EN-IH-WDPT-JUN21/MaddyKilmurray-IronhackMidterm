package com.ironhack.midterm.dao.accounts.accountsubclasses;

import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudentCheckingAccount extends Account {

    private static final String accountType = "Student Checking Account";

    @NotBlank
    @Column(name = "secret_key")
    private String secretKey;

    public StudentCheckingAccount(Money balance, String secretKey, AccountHolder primaryOwner) {
        this.balance = balance;
        this.secretKey = secretKey;
        this.primaryOwner = primaryOwner;

        this.creationDate = LocalDate.now();
        setStandardPenaltyFee();
        this.status = Status.ACTIVE;
    }

    public StudentCheckingAccount(Money balance, String secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.secretKey = secretKey;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;

        this.creationDate = LocalDate.now();
        setStandardPenaltyFee();
        this.status = Status.ACTIVE;
    }

    public void setStandardPenaltyFee() {
        this.penaltyFee = new Money(new BigDecimal(40));
    }
}
