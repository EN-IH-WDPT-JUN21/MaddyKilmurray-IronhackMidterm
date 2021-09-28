package com.ironhack.midterm.controller.dto.accounts;

import com.ironhack.midterm.dao.Constants;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckingAccountDTO extends AccountDTO {

    @NotBlank
    @Column(name = "secret_key")
    private String secretKey;

    public CheckingAccountDTO(Money balance, Long primaryOwnerId, String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secretKey = secretKey;
    }

    public CheckingAccountDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String secretKey) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.secretKey = secretKey;
    }
}
