package com.ironhack.midterm.dao.accounts.accountsubclasses;

import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.enums.Status;
import com.ironhack.midterm.exceptions.BalanceOutOfBoundsException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    public CreditCardAccount(Money balance, AccountHolder primaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;

        setDefaultInterestRate();
        setDefaultPenaltyFee();
        setDefaultCreditLimit();
        this.creationDate = LocalDate.now();
        this.status = Status.ACTIVE;
    }

    public CreditCardAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;

        setDefaultInterestRate();
        setDefaultPenaltyFee();
        setDefaultCreditLimit();
        this.creationDate = LocalDate.now();
        this.status = Status.ACTIVE;
    }

    public CreditCardAccount(Money balance, AccountHolder primaryOwner, Money creditLimit, Money interestRate) throws BalanceOutOfBoundsException {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);

        setDefaultPenaltyFee();
        this.creationDate = LocalDate.now();
        this.status = Status.ACTIVE;
    }

    public void setDefaultPenaltyFee() {
        this.penaltyFee = new Money(new BigDecimal(40));
    }

    public void setDefaultInterestRate() {
        this.interestRate = new Money(new BigDecimal(0.2));
    }

    public void setInterestRate(Money interestRate) throws BalanceOutOfBoundsException {
        BigDecimal minLimit = new BigDecimal(0.1);
        BigDecimal maxLimit = new BigDecimal(0.2);
        if (interestRate.getAmount().compareTo(minLimit) >= 0 && interestRate.getAmount().compareTo(minLimit) <= -1) {
            this.interestRate = creditLimit;
        }
        else {
            throw new BalanceOutOfBoundsException("Credit card interest rates must be between 0.1 and 0.2. Please try again.");
        }
    }

    public void setCreditLimit(Money creditLimit) throws BalanceOutOfBoundsException {
        BigDecimal minLimit = new BigDecimal(100);
        BigDecimal maxLimit = new BigDecimal(10000);
        if (creditLimit.getAmount().compareTo(minLimit) >= 0 && creditLimit.getAmount().compareTo(minLimit) <= -1) {
            this.creditLimit = creditLimit;
        }
        else {
            throw new BalanceOutOfBoundsException("That credit limit is out of bounds. Your credit limit must be between 100 and 100000. Please try again.");
        }
    }

    public void setDefaultCreditLimit() {
        this.creditLimit = new Money(new BigDecimal(100));
    }
}
