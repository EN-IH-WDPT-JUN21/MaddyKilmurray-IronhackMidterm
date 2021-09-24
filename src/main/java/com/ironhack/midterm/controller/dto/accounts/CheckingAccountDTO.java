package com.ironhack.midterm.controller.dto.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckingAccountDTO {

    private long id;

    @NotNull
    private Money balance;

    @NotNull
    private AccountHolder primaryOwner;

    private AccountHolder secondaryOwner;

    @NotBlank
    private String secretKey;
}
