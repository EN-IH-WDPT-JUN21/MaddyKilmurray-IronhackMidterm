package com.ironhack.midterm.controller.dto.accounts;

import com.ironhack.midterm.dao.Constants;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.users.usersubclasses.ThirdParty;
import com.ironhack.midterm.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyAccountDTO {

    private long id;

    @NotNull
    private Money balance;

    @NotNull
    private Long primaryOwnerId;

    private Long secondaryOwnerId;

    @NotBlank
    private String hashedKey;

    @NotNull
    private String name;

    public ThirdPartyAccountDTO(Money balance, Long primaryOwnerId, String hashedKey, String name) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.hashedKey = hashedKey;
        this.name = name;
    }

    public ThirdPartyAccountDTO(Money balance, Long primaryOwnerId, Long secondaryOwnerId, String hashedKey, String name) {
        this.balance = balance;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.hashedKey = hashedKey;
        this.name = name;
    }
}
