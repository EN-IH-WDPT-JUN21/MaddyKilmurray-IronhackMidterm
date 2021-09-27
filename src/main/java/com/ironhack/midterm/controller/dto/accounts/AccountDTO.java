package com.ironhack.midterm.controller.dto.accounts;

import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    protected long id;

    @NotNull
    protected Money balance;

    @NotNull
    protected Long primaryOwnerId;

    protected Long secondaryOwnerId;

    public AccountDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
    }

    public AccountDTO(Money balance, Long primaryOwnerId) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
    }
}
