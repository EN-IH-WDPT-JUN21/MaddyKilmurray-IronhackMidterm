package com.ironhack.midterm.controller.dto.accounts;

import com.ironhack.midterm.dao.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SavingsAccountDTO extends AccountDTO {

    private String secretKey;

    @NotNull
    private Money interestRate;

    public SavingsAccountDTO(Money balance, Long primaryOwnerId, String secretKey, Money interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
        this.interestRate = interestRate;
    }

    public SavingsAccountDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String secretKey, Money interestRate) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
        this.interestRate = interestRate;
    }

    public SavingsAccountDTO(Money balance, Long primaryOwnerId, String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
    }

    public SavingsAccountDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
    }

}
