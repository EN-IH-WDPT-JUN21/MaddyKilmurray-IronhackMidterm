package com.ironhack.midterm.controller.dto.accounts;

import com.ironhack.midterm.dao.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public CreditCardAccountDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, Money creditLimit) {
        super(balance, primaryOwnerId, secondaryOwnerId);
        this.creditLimit = creditLimit;
    }
}
