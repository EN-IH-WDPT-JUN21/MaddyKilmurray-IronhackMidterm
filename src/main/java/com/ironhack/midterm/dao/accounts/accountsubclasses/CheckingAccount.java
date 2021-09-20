package com.ironhack.midterm.dao.accounts.accountsubclasses;

import com.ironhack.midterm.dao.Constants;
import com.ironhack.midterm.dao.accounts.Account;
import com.ironhack.midterm.dao.users.usersubclasses.AccountHolder;
import com.ironhack.midterm.dao.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Currency;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("CHECKING")
public class CheckingAccount extends Account {

    private static final String accountType = "Checking Account";

    @NotBlank
    @Column(name = "secret_key")
    private String secretKey;

    @NotNull
    @Column(name = "minimum_balance")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "amount", column = @Column(name = "minimum_balance")),
            @AttributeOverride( name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private Money minimumBalance;

    @Column(name = "monthly_maintenance_fee")
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "amount", column = @Column(name = "monthly_maintenance_fee")),
            @AttributeOverride( name = "currency", column = @Column(name = "maintenance_fee_currency"))
    })
    private Money monthlyMaintenanceFee;

    public CheckingAccount(Money balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        this.secretKey = generateSecretKey(secretKey);
        this.minimumBalance = new Money(Constants.CHECKING_MINIMUM_BALANCE, Currency.getInstance("GBP"));
        this.monthlyMaintenanceFee = new Money(Constants.CHECKING_MAINTENANCE_FEE,Currency.getInstance("GBP"));
    }

    public CheckingAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = generateSecretKey(secretKey);
        this.minimumBalance = new Money(Constants.CHECKING_MINIMUM_BALANCE, Currency.getInstance("GBP"));
        this.monthlyMaintenanceFee = new Money(Constants.CHECKING_MAINTENANCE_FEE,Currency.getInstance("GBP"));
    }
}
