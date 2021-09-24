package com.ironhack.midterm.dao.accounts.accountsubclasses;

import com.ironhack.midterm.dao.Constants;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.User;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.exceptions.BalanceOutOfBoundsException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("SAVINGS")
public class SavingsAccount extends Account {

    private static final String accountType = "Savings Account";

    @NotBlank
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

    private LocalDate interestLastApplied = null;

    public SavingsAccount(Money balance, AccountHolder primaryOwner, String secretKey, Money interestRate) throws BalanceOutOfBoundsException {
        super(balance);
        this.primaryOwner = primaryOwner;
        setBalance(balance);
        generateSecretKey(secretKey);
        setInterestRate(interestRate);
        this.interestLastApplied = creationDate;
    }

    public SavingsAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, Money interestRate) throws BalanceOutOfBoundsException {
        super(balance);
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        setBalance(balance);
        generateSecretKey(secretKey);
        setInterestRate(interestRate);
        this.interestLastApplied = creationDate;
    }

    public SavingsAccount(Money balance, AccountHolder primaryOwner, String secretKey) throws BalanceOutOfBoundsException {
        super(balance);
        this.primaryOwner = primaryOwner;
        setBalance(balance);
        generateSecretKey(secretKey);
        this.interestRate = new Money(Constants.SAVINGS_DEFAULT_INTEREST_RATE, Currency.getInstance("GBP"));
        this.interestLastApplied = creationDate;
    }

    public SavingsAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) throws BalanceOutOfBoundsException {
        super(balance);
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        setBalance(balance);
        generateSecretKey(secretKey);
        this.interestRate = new Money(Constants.SAVINGS_DEFAULT_INTEREST_RATE, Currency.getInstance("GBP"));
        this.interestLastApplied = creationDate;
    }

    @Override
    public void setBalance(Money balance) {
        if (balance.getAmount().compareTo(Constants.SAVINGS_ABSOLUTE_MINIMUM_BALANCE) == 1) {
            this.balance = balance;
        }
        else {
            System.out.println("Balance out of bounds exception, work it out.");;
        }
    }

    public void setInterestRate(Money interestRate) throws BalanceOutOfBoundsException {
        if (interestRate.getAmount().compareTo(Constants.SAVINGS_DEFAULT_INTEREST_RATE) >= 0 &&
                interestRate.getAmount().compareTo(Constants.SAVINGS_MAXIMUM_INTEREST_RATE) <= -1) {
            this.interestRate = interestRate;
        }
        else {
            throw new BalanceOutOfBoundsException("Savings Account interest must be between 0.0025 and 0.5. Please try again.");
        }
    }

    public static void applyYearlyInterest(SavingsAccount account) {
        BigDecimal yearsBetween = new BigDecimal(ChronoUnit.YEARS.between(
                account.getInterestLastApplied(),
                LocalDate.now()
        )).setScale(0, RoundingMode.DOWN);
        if (yearsBetween.compareTo(BigDecimal.valueOf(1).setScale(0,RoundingMode.UP)) >= 0) {
            BigDecimal newBalance = account.getBalance().getAmount().add(account.getInterestRate().getAmount().multiply(yearsBetween));
            account.setBalance(new Money(newBalance, Currency.getInstance("GBP")));
        }
    }
}
