package com.ironhack.midterm.dao.accounts;

import com.ironhack.midterm.dao.users.AccountHolder;
import com.ironhack.midterm.dao.Money;
import com.ironhack.midterm.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CheckingAccount extends Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    public CheckingAccount(Money balance, String secretKey, AccountHolder primaryOwner) {
        this.balance = balance;
        this.secretKey = secretKey;
        this.primaryOwner = primaryOwner;

        this.creationDate = LocalDate.now();
        setStandardPenaltyFee();
        setStandardMaintanenceFee();
        setStandardMinimumBalance();
        this.status = Status.ACTIVE;
    }

    public CheckingAccount(Money balance, String secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.secretKey = secretKey;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;

        this.creationDate = LocalDate.now();
        setStandardPenaltyFee();
        setStandardMaintanenceFee();
        setStandardMinimumBalance();
        this.status = Status.ACTIVE;
    }

    public void setStandardMinimumBalance() {
        this.minimumBalance = new Money(new BigDecimal(250));
    }

    public void setStandardMaintanenceFee() {
        this.monthlyMaintenanceFee = new Money(new BigDecimal(12));
    }

    public void setStandardPenaltyFee() {
        this.penaltyFee = new Money(new BigDecimal(40));
    }
}
