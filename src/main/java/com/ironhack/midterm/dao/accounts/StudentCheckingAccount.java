package com.ironhack.midterm.dao.accounts;

import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.users.AccountHolder;
import com.ironhack.midterm.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudentCheckingAccount extends Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
