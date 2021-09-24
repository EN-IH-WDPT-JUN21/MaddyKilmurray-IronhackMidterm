package com.ironhack.midterm.controller.dto.accounts;

import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SavingsAccountDTO {

    private long id;

    @NotNull
    private Money balance;

    @NotNull
    private AccountHolder primaryOwner;

    private AccountHolder secondaryOwner;

    @NotBlank
    private String secretKey;

    private Money interestRate;
}
