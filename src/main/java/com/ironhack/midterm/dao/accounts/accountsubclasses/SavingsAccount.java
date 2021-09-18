package com.ironhack.midterm.dao.accounts.accountsubclasses;

import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class SavingsAccount extends Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(name = "secret_key")
    private String secretKey;

    @NotNull
    @Column(name = "minimum_balance")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "amount", column = @Column(name = "minimum_balance")),
            @AttributeOverride( name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private Money minimumBalance;

    @NotNull
    @Column(name = "interest_rate")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "amount", column = @Column(name = "interest_rate")),
            @AttributeOverride( name = "currency", column = @Column(name = "interest_rate_currency"))
    })
    private Money interestRate;

    public SavingsAccount(Money balance, String secretKey, AccountHolder primaryOwner) {
        this.balance = balance;
        this.secretKey = secretKey;
        this.primaryOwner = primaryOwner;

        this.creationDate = LocalDate.now();
        setStandardPenaltyFee();
        setStandardMinimumBalance();
        this.status = Status.ACTIVE;
    }

    public SavingsAccount(Money balance, String secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.secretKey = secretKey;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;

        this.creationDate = LocalDate.now();
        setStandardPenaltyFee();
        setStandardMinimumBalance();
        this.status = Status.ACTIVE;
    }

    @Override
    public void setBalance(Money balance) {
        BigDecimal minBalance = new BigDecimal(99);
        if (balance.getAmount().compareTo(minBalance) == 1) {
            this.balance = balance;
        }
        else {
            System.out.println("Balance out of bounds exception, work it out.");;
        }
    }

    public void setStandardMinimumBalance() {
        this.minimumBalance = new Money(new BigDecimal(1000));
    }

    public void setStandardPenaltyFee() {
        this.penaltyFee = new Money(new BigDecimal(40));
    }

    public void setStandardInterestRate() {
        this.interestRate = new Money(new BigDecimal(0.0025));
    }
}
