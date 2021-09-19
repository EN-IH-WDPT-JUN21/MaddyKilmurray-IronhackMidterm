package com.ironhack.midterm.dao.accounts.accountsubclasses;

import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("Third_Party")
public class ThirdPartyAccount extends Account {

    @NotNull
    private String hashedKey;

    @NotNull
    private String name;

    public ThirdPartyAccount(Money balance, AccountHolder primaryOwner, String hashedKey, String name) {
        super(balance, primaryOwner);
        this.hashedKey = generateSecretKey(hashedKey);
        this.name = name;
    }

    public ThirdPartyAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String hashedKey, String name) {
        super(balance, primaryOwner, secondaryOwner);
        this.hashedKey = generateSecretKey(hashedKey);
        this.name = name;
    }

}
