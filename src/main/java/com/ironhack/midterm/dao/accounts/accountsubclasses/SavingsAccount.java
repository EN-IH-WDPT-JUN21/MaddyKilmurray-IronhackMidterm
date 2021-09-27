package com.ironhack.midterm.dao.accounts.accountsubclasses;

import com.ironhack.midterm.dao.Constants;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.exceptions.BalanceOutOfBoundsException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("SAVINGS")
public class SavingsAccount extends Account {

    @Column(name = "secret_key")
    private String secretKey;

    @NotNull
    @Column(name = "interest_rate")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "amount", column = @Column(name = "interest_rate")),
            @AttributeOverride( name = "currency", column = @Column(name = "interest_rate_currency"))
    })
    private Money interestRate;

    private LocalDate savingsInterestLastApplied = null;

    public SavingsAccount(Money balance, AccountHolder primaryOwner, String secretKey, Money interestRate) {
        super(balance);
        this.primaryOwner = primaryOwner;
        setBalance(balance);
        this.secretKey = secretKey;
        setInterestRate(interestRate);
        this.savingsInterestLastApplied = creationDate;
    }

    public SavingsAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, Money interestRate) {
        super(balance);
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        setBalance(balance);
        this.secretKey = secretKey;
        setInterestRate(interestRate);
        this.savingsInterestLastApplied = creationDate;
    }

    public SavingsAccount(Money balance, AccountHolder primaryOwner, String secretKey) {
        super(balance);
        this.primaryOwner = primaryOwner;
        setBalance(balance);
        this.secretKey = secretKey;
        this.interestRate = new Money(Constants.SAVINGS_DEFAULT_INTEREST_RATE, Currency.getInstance("GBP"));
        this.savingsInterestLastApplied = creationDate;
    }

    public SavingsAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance);
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        setBalance(balance);
        this.secretKey = secretKey;
        this.interestRate = new Money(Constants.SAVINGS_DEFAULT_INTEREST_RATE, Currency.getInstance("GBP"));
        this.savingsInterestLastApplied = creationDate;
    }

    @Override
    public void setBalance(Money balance) {
        if (balance.getAmount().compareTo(Constants.SAVINGS_ABSOLUTE_MINIMUM_BALANCE) == 1) {
            this.balance = balance;
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Balance out of bounds. Savings minimum balance must be £100. Please try again.");
        }
    }

    public void setInterestRate(Money interestRate) {
        if (interestRate.getAmount().compareTo(Constants.SAVINGS_DEFAULT_INTEREST_RATE) >= 0 &&
                interestRate.getAmount().compareTo(Constants.SAVINGS_MAXIMUM_INTEREST_RATE) <= -1) {
            this.interestRate = interestRate;
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Savings Account interest must be between 0.0025 and 0.5. Please try again.");
        }
    }
}
