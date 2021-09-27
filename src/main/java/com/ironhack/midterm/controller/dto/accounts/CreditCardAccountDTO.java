package com.ironhack.midterm.controller.dto.accounts;

import com.ironhack.midterm.dao.Constants;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.exceptions.BalanceOutOfBoundsException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;
import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardAccountDTO extends AccountDTO {

    private Money creditLimit;

    private Money interestRate;

    public CreditCardAccountDTO(Money balance, Long primaryOwnerId, Money creditLimit, Money interestRate) {
        super(balance,primaryOwnerId);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCardAccountDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, Money creditLimit, Money interestRate) {
        super(balance,primaryOwnerId, secondaryOwnerId);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCardAccountDTO(Money balance, Long primaryOwnerId) {
        super(balance, primaryOwnerId);
    }

    public CreditCardAccountDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId) {
        super(balance, primaryOwnerId,secondaryOwnerId);
    }

    public CreditCardAccountDTO(Money balance, Long primaryOwnerId, Money creditLimit) {
        super(balance, primaryOwnerId);
        this.creditLimit = creditLimit;
    }

    public CreditCardAccountDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, Money creditLimit) throws BalanceOutOfBoundsException {
        super(balance, primaryOwnerId, secondaryOwnerId);
        this.creditLimit = creditLimit;
    }
}
