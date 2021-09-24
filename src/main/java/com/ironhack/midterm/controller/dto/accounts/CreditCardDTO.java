package com.ironhack.midterm.controller.dto.accounts;

import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDTO {

    private long id;

    @NotNull
    private Money balance;

    @NotNull
    private AccountHolder primaryOwner;

    private AccountHolder secondaryOwner;

    private Money creditLimit;

    private Money interestRate;
}
