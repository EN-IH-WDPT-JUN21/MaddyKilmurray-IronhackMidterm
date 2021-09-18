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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CreditCardAccount extends Account {

    private static final String accountType = "Credit Card Account";

    @NotNull
    @Column(name = "credit_limit")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "amount", column = @Column(name = "credit_limit")),
            @AttributeOverride( name = "currency", column = @Column(name = "credit_limit_currency"))
    })
    private Money creditLimit;

    @NotNull
    @Column(name = "interest_rate")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "amount", column = @Column(name = "interest_rate")),
            @AttributeOverride( name = "currency", column = @Column(name = "interest_rate_currency"))
    })
    private Money interestRate;

    public CreditCardAccount(Money balance, AccountHolder primaryOwner, Money creditLimit, Money interestRate) throws BalanceOutOfBoundsException {
        super(balance, primaryOwner);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    public CreditCardAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit, Money interestRate) throws BalanceOutOfBoundsException {
        super(balance, primaryOwner,secondaryOwner);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    public CreditCardAccount(Money balance, AccountHolder primaryOwner) {
        super(balance, primaryOwner);
        this.creditLimit = new Money(Constants.CCARD_DEFAULT_CREDITLIMIT, Currency.getInstance("GBP"));
        this.interestRate = new Money(Constants.CCARD_DEFAULT_INTEREST_RATE, Currency.getInstance("GBP"));
    }

    public CreditCardAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
        this.creditLimit = new Money(Constants.CCARD_DEFAULT_CREDITLIMIT, Currency.getInstance("GBP"));
        this.interestRate = new Money(Constants.CCARD_DEFAULT_INTEREST_RATE, Currency.getInstance("GBP"));
    }

    public CreditCardAccount(Money balance, AccountHolder primaryOwner, Money creditLimit) throws BalanceOutOfBoundsException {
        super(balance, primaryOwner);
        setCreditLimit(creditLimit);
        this.interestRate = new Money(Constants.CCARD_DEFAULT_INTEREST_RATE, Currency.getInstance("GBP"));
    }

    public CreditCardAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money creditLimit) throws BalanceOutOfBoundsException {
        super(balance, primaryOwner, secondaryOwner);
        setCreditLimit(creditLimit);
        this.interestRate = new Money(Constants.CCARD_DEFAULT_INTEREST_RATE, Currency.getInstance("GBP"));
    }

    public void setInterestRate(Money interestRate) throws BalanceOutOfBoundsException {
        if (interestRate.getAmount().compareTo(Constants.CCARD_DEFAULT_CREDITLIMIT) >= 0 &&
                interestRate.getAmount().compareTo(Constants.CCARD_MAXIMUM_CREDITLIMIT) <= -1) {
            this.interestRate = interestRate;
        }
        else {
            throw new BalanceOutOfBoundsException("Credit card interest rates must be between 0.1 and 0.2. Please try again.");
        }
    }

    public void setCreditLimit(Money creditLimit) throws BalanceOutOfBoundsException {
        if (creditLimit.getAmount().compareTo(Constants.CCARD_MINIMUM_INTEREST_RATE) >= 0 &&
                creditLimit.getAmount().compareTo(Constants.CCARD_DEFAULT_INTEREST_RATE) <= -1) {
            this.creditLimit = creditLimit;
        }
        else {
            throw new BalanceOutOfBoundsException("That credit limit is out of bounds. Your credit limit must be between 100 and 100000. Please try again.");
        }
    }
}
